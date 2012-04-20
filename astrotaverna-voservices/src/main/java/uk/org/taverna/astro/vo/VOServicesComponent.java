package uk.org.taverna.astro.vo;

import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import org.apache.log4j.Logger;

import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import uk.org.taverna.astro.vorepo.VORepository;

public class VOServicesComponent extends JPanel implements UIComponentSPI {
	private static final int RESOURCE_COLUMN = 5;
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
			// TODO: Make the real service
			VOServiceDescription restServiceDescription = new VOServiceDescription();
			WorkflowView
					.importServiceDescription(restServiceDescription, false);
			Workbench.getInstance().getPerspectives().setWorkflowPerspective();
		}

	}

	private SearchTask searchTask;

	private final class SearchTask extends SwingWorker<List<Service>, String> {
		private String search;

		public SearchTask(String search) {
			this.search = search;
		}

		@Override
		protected List<Service> doInBackground() throws Exception {
			return repo.resourceSearch(
					net.ivoa.xml.conesearch.v1.ConeSearch.class,
					search.split(" "));
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
			status.setText(resources.size() + " results for: " + search);
			for (Service r : resources) {
				String shortName = r.getShortName();
				String title = r.getTitle();
				String subjects = r.getContent().getSubject().toString();
				String identifier = r.getIdentifier();
				String publisher = r.getCuration().getPublisher().getValue();
				resultsTableModel.addRow(new Object[] { shortName, title,
						subjects, identifier, publisher, r });
			}
			if (searchTask == this) {
				searchTask = null;
			}
		}

	}

	public class ConeSearch extends AbstractAction {

		public ConeSearch() {
			super("Cone Search");
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			cancelSearchTask();
			String search = keywords.getText();
			status.setText("Searching: " + search);

			searchTask = new SearchTask(search);
			int rows = resultsTableModel.getRowCount();
			while (rows > 0) {
				resultsTableModel.removeRow(--rows);
			}
			resultsDetails.removeAll();
			results.validate();
			searchTask.execute();
		}
	}

	public class SIASearch extends AbstractAction {

		public SIASearch() {
			super("SIA Search");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public class SSASearch extends AbstractAction {

		public SSASearch() {
			super("SSA Search");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

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
		gbc.weighty = 1.0;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		add(new JPanel(), gbc);
		
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
		status = new JLabel("Searched for: fred");
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
		resultsTableModel.addColumn("Short name");
		resultsTableModel.addColumn("Title");
		resultsTableModel.addColumn("Subjects");
		resultsTableModel.addColumn("Identifier");
		resultsTableModel.addColumn("Publisher");
		resultsTableModel.addColumn("Service");
		

		JTable resultsTable = new JTable(resultsTableModel);
		// resultsTable.setAutoCreateColumnsFromModel(true);
		resultsTable.setAutoCreateRowSorter(true);
		// resultsTable.createDefaultColumnsFromModel();
		resultsTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		resultsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {

						updateDetails((Service) resultsTableModel.getValueAt(
								e.getFirstIndex(), RESOURCE_COLUMN));
					}
				});

		return new JScrollPane(resultsTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	protected void updateDetails(Service service) {		
		results.invalidate();
		resultsDetails.removeAll();
		results.validate();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;		
		
		String message = String.format("<html><body><h3>%s: %s</h3>"
				+ "<p>%s</p>" + "<dl><dt>Provider</dt> <dd>%s</dd>"
				+ "  <dt>Documentation</dt> <dd><a href='%s'>%s</a></dd>"
				+ "</dl>", service.getShortName(), service.getTitle(), service
				.getContent().getDescription(), service.getCuration()
				.getPublisher().getValue(), service.getContent()
				.getReferenceURL(), service.getContent().getReferenceURL());
		resultsDetails.add(new JLabel(message), gbc);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(new JButton(new AddToWorkflow(service)), gbc);
		resultsDetails.add(buttonPanel, gbc);
		
		gbc.weighty = 1.0;
		JPanel filler = new JPanel();
		//filler.setBackground(Color.red);
		resultsDetails.add(filler, gbc); // filler
		resultsDetails.invalidate();
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
//		searchBox.add(new JPanel(), filler);

		return searchBox;

	}

	ConeSearch coneSearch = new ConeSearch();
	private JPanel resultsDetails;
	private JSplitPane results;

	protected JPanel makeSearchButtons() {
		JPanel searchButtons = new JPanel(new FlowLayout());

		searchButtons.add(new JButton(coneSearch));
		searchButtons.add(new JButton(new SIASearch()));
		searchButtons.add(new JButton(new SSASearch()));
		return searchButtons;
	}

	protected List<URI> getRegistries() {
		return Arrays.asList(URI.create("http://example.com/"),
				URI.create("http://example.org/service/registry"),
				URI.create("http://example.org/service/registry/asdfasdfsafd"));
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
	}

}
