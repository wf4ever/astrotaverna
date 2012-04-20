package uk.org.taverna.astro.vo;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.WebService;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;

import org.apache.log4j.Logger;

import uk.org.taverna.astro.vorepo.VORepository;

public class VOServicesComponent extends JPanel implements UIComponentSPI {
	private static final int RESOURCE_COLUMN = 0;
	private static Logger logger = Logger.getLogger(VOServicesComponent.class);
	private VORepository repo = new VORepository();
	private Edits edits = EditsRegistry.getEdits();
	private FileManager fileManager = FileManager.getInstance();

	public class AddToWorkflow extends AbstractAction {

		private final Service service;

		public AddToWorkflow(Service service) {
			super(String.format("Add %s to workflow", service.getShortName()));
			this.service = service;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			VOServiceDescription restServiceDescription = new VOServiceDescription();

			for (Capability c : service.getCapability()) {
				if (!(searchType.isInstance(c))) {
					continue;
				}
				for (Interface i : c.getInterface()) {
					if (i instanceof ParamHTTP) {
						ParamHTTP http = (ParamHTTP) i;
						AccessURL accessURL = http.getAccessURL().get(0);
						restServiceDescription.setAccessURL(accessURL
								.getValue().trim());
						restServiceDescription.setIdentifier(URI.create(service
								.getIdentifier().trim()));
						restServiceDescription.setName(service.getShortName());
						restServiceDescription.setSearchType(searchType
								.getSimpleName());
						break;
					}
					if (i instanceof WebService) {
						// TODO: Potentially loads to check here
					}
				}
			}
			if (restServiceDescription.getAccessURL() == null) {
				// TODO: Show error

				return;
			}

			WorkflowView
					.importServiceDescription(restServiceDescription, false);
			Workbench.getInstance().getPerspectives().setWorkflowPerspective();
		}

	}

	private SearchTask searchTask;
	public Class<? extends Capability> searchType;

	private final class SearchTask extends SwingWorker<List<Service>, String> {
		private String search;
		private final Class<? extends Capability> searchType;

		public SearchTask(Class<? extends Capability> searchType, String search) {
			this.searchType = searchType;
			this.search = search;
		}

		@Override
		protected List<Service> doInBackground() throws Exception {
			return repo.resourceSearch(searchType, search.split(" "));
		}

		@Override
		protected void done() {
			List<Service> resources;
			try {
				resources = get();
			} catch (CancellationException ex) {
				logger.info("Cancelled search", ex);
				status.setText("<html><body><font color='#dd2222'>"
						+ "Cancelled search" + "</font><body></html>");
				return;
			} catch (InterruptedException ex) {
				logger.warn("Interrupted search", ex);
				status.setText("<html><body><font color='#dd2222'>"
						+ "Search interrupted: " + ex.getLocalizedMessage()
						+ "</font><body></html>");
				return;
			} catch (ExecutionException ex) {
				logger.warn("Failed search", ex);
				status.setText("<html><body><font color='#dd2222'>"
						+ "Search failed: " + ex.getLocalizedMessage()
						+ "</font><body></html>");
				return;
			}
			status.setText(String.format("%d results for %s: %s",
					resources.size(), searchType.getSimpleName(), search));
			VOServicesComponent.this.searchType = searchType;
			for (Service r : resources) {
				String shortName = r.getShortName();
				String title = r.getTitle();
				String subjects = r.getContent().getSubject().toString();
				String identifier = r.getIdentifier();
				String publisher = r.getCuration().getPublisher().getValue();
				resultsTableModel.addRow(new Object[] { r, shortName, title,
						subjects, identifier, publisher });
			}

			if (searchTask == this) {
				searchTask = null;
			}
		}
	}

	public class SearchAction extends AbstractAction {

		protected Class<? extends Capability> searchType;

		public SearchAction(String label) {
			super(label);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			cancelSearchTask();
			String search = keywords.getText();
			status.setText(String.format("%s: %s", searchType.getSimpleName(),
					search));

			searchTask = new SearchTask(searchType, search);
			int rows = resultsTableModel.getRowCount();
			while (rows > 0) {
				resultsTableModel.removeRow(--rows);
			}
			resultsDetails.removeAll();
			results.validate();
			searchTask.execute();
		}
	}

	public class ConeSearchAction extends SearchAction {

		public ConeSearchAction() {
			super("Cone Search");
			this.searchType = ConeSearch.class;
		}
	}

	public class SIASearchAction extends SearchAction {

		public SIASearchAction() {
			super("SIA Search");
			this.searchType = SimpleImageAccess.class;
		}
	}

	public class SSASearchAction extends SearchAction {

		public SSASearchAction() {
			super("SSA Search");
			this.searchType = SimpleSpectralAccess.class;
		}
	}

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	private JComboBox registry;
	private JTextField keywords;
	private DefaultTableModel resultsTableModel;
	private JLabel status;

	public VOServicesComponent() {
		initialize();

	}

	protected void initialize() {
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		add(makeSearchBox(), gbc);
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new JPanel(), gbc);//filler

		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		add(makeResults(), gbc);

	}

	protected Component makeResults() {
		JPanel resultsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		status = new JLabel("");
		resultsPanel.add(status, gbc);

		results = new JSplitPane();
		results.setLeftComponent(makeResultsTable());
		results.setRightComponent(makeResultsDetails());
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		resultsPanel.add(results, gbc);
		return resultsPanel;
	}

	protected Component makeResultsDetails() {
		resultsDetails = new JPanel(new GridBagLayout());
		return resultsDetails;
	}

	protected Component makeResultsTable() {
		resultsTableModel = new DefaultTableModel();
		resultsTableModel.addColumn("Service");
		resultsTableModel.addColumn("Short name");
		resultsTableModel.addColumn("Title");
		resultsTableModel.addColumn("Subjects");
		resultsTableModel.addColumn("Identifier");
		resultsTableModel.addColumn("Publisher");

		resultsTable = new JTable(resultsTableModel);
		// resultsTable.setAutoCreateColumnsFromModel(true);
		resultsTable.setAutoCreateRowSorter(true);
		// resultsTable.createDefaultColumnsFromModel();
		resultsTable.removeColumn(resultsTable.getColumn("Service"));

		resultsTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		resultsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							return;
						}
						int selection = resultsTable.getSelectionModel()
								.getMinSelectionIndex();
						if (selection < 0) {
							updateDetails(null);
							return;
						}
						updateDetails((Service) resultsTableModel.getValueAt(
								selection, RESOURCE_COLUMN));
					}
				});

		return new JScrollPane(resultsTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	protected void updateDetails(Service service) {
		resultsDetails.removeAll();
		if (service == null) {
			results.validate();
			return;
		}
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		String message = String.format("<html><body><h3>%s: %s</h3>"
				+ "<p>%s</p>" + "<dl><dt>Publisher</dt> <dd>%s</dd>"
				+ "  <dt>Documentation</dt> <dd><a href='%s'>%s</a></dd>"
				+ "</dl>", service.getShortName(), service.getTitle(), service
				.getContent().getDescription(), service.getCuration()
				.getPublisher().getValue(), service.getContent()
				.getReferenceURL(), service.getContent().getReferenceURL());
		JEditorPane htmlPane = new JEditorPane("text/html", message);
		htmlPane.setEditable(false);
		resultsDetails.add(new JScrollPane(htmlPane), gbc);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new JButton(new AddToWorkflow(service)), gbc);
		resultsDetails.add(buttonPanel, gbc);

		gbc.weighty = 0.1;
		JPanel filler = new JPanel();
		// resultsDetails.add(filler, gbc); // filler
		results.validate();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Component makeSearchBox() {
		JPanel searchBox = new JPanel(new GridBagLayout());
		GridBagConstraints gbcLeft = new GridBagConstraints();
		GridBagConstraints gbcMiddle = new GridBagConstraints();
		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcLeft.gridx = 0;
		gbcLeft.anchor = GridBagConstraints.LINE_END;
		searchBox.add(new JLabel("Registry:"), gbcLeft);
		gbcMiddle.anchor = GridBagConstraints.WEST;
		gbcMiddle.fill = GridBagConstraints.HORIZONTAL;
		gbcMiddle.gridx = 1;
		gbcMiddle.weightx = 0.1;

		URI[] registries = getRegistries().toArray(new URI[0]);
		registry = new JComboBox(registries);
		registry.setEditable(true);

		searchBox.add(registry, gbcMiddle);

		searchBox.add(new JLabel("Keywords:"), gbcLeft);
		keywords = new JTextField(40);
		keywords.setAction(coneSearch);
		searchBox.add(keywords, gbcMiddle);

		gbcRight.gridx = 1;
		gbcRight.gridy = 2;
		gbcRight.anchor = GridBagConstraints.WEST;
		searchBox.add(makeSearchButtons(), gbcRight);

		GridBagConstraints filler = new GridBagConstraints();
		filler.gridx = 3;
		filler.weightx = 1.0;
		filler.fill = GridBagConstraints.HORIZONTAL;
		// searchBox.add(new JPanel(), filler);

		return searchBox;

	}

	ConeSearchAction coneSearch = new ConeSearchAction();
	SIASearchAction siaSearchAction = new SIASearchAction();
	SSASearchAction ssaSearchAction = new SSASearchAction();

	private JPanel resultsDetails;
	private JSplitPane results;
	private JTable resultsTable;

	protected JPanel makeSearchButtons() {
		JPanel searchButtons = new JPanel(new FlowLayout());

		searchButtons.add(new JButton(coneSearch));
		searchButtons.add(new JButton(siaSearchAction));
		searchButtons.add(new JButton(ssaSearchAction));
		return searchButtons;
	}

	protected List<URI> getRegistries() {
		return Arrays.asList(VORepository.DEFAULT_ENDPOINT);
	}

	@Override
	public ImageIcon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	@Override
	public void onDisplay() {

	}

	@Override
	public void onDispose() {
		cancelSearchTask();
	}

	private void cancelSearchTask() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		searchType = null;
	}

}
