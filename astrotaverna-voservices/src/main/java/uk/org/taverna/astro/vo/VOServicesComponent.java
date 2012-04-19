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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import net.ivoa.xml.voresource.v1.Resource;
import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;
import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.EditsRegistry;
import uk.org.taverna.astro.vorepo.VORepository;

public class VOServicesComponent extends JPanel implements UIComponentSPI {
	private VORepository repo = new VORepository();
	private Edits edits = EditsRegistry.getEdits();
	private FileManager fileManager = FileManager.getInstance();

	public class AddToWorkflow extends AbstractAction {

		public AddToWorkflow() {
			super("Add to workflow");
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

	public class ConeSearch extends AbstractAction {

		private SearchTask searchTask;

		private final class SearchTask extends
				SwingWorker<List<Service>, String> {
			private String search;

			public SearchTask(String search) {
				this.search = search;
			}

			@Override
			protected List<Service> doInBackground() throws Exception {
				return repo.resourceSearch(net.ivoa.xml.conesearch.v1.ConeSearch.class, search.split(" "));
			}

			@Override
			protected void done() {
				List<Service> resources;
				try {
					resources = get();
				} catch (CancellationException ex) {
					status.setText("<html><body><font color='#dd2222'>"
							+ "Cancelled search"
							+ "</font><body></html>");
					return;
				} catch (InterruptedException ex) {
					status.setText("<html><body><font color='#dd2222'>"
							+ "Search interrupted: "
							+ ex.getLocalizedMessage()
							+ "</font><body></html>");
					return;
				} catch (ExecutionException ex) {
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
					resultsTableModel.addRow(new String[] { shortName,
							title, subjects, identifier, publisher });
				}
				if (searchTask == this) {
					searchTask = null;
				}
			}
		}

		public ConeSearch() {
			super("Cone Search");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
 
			if (searchTask != null) { 
				searchTask.cancel(true);
			}
			String search = keywords.getText();
			status.setText("Searching: " + search);
			
			searchTask = new SearchTask(search);
			int rows = resultsTableModel.getRowCount();
			while (rows > 0) {
				resultsTableModel.removeRow(--rows);
			}
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
		add(makeSearchBox(), gbc);
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
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

		JSplitPane results = new JSplitPane();
		results.setLeftComponent(makeResultsTable());
		results.setRightComponent(makeResultsDetails());
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		resultsPanel.add(results, gbc);

		return resultsPanel;
	}

	protected Component makeResultsDetails() {
		JPanel jPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		jPanel.add(new JLabel("<html><body><h3>Details for service X</h3>"
				+ "<dl><dt>Provider</dt> <dd>The factory</dd>"
				+ "  <dt>Documentation</dt> <dd>http://example.com/</dd>"
				+ "</dl>"), gbc);

		jPanel.add(new JButton(new AddToWorkflow()), gbc);
		return jPanel;
	}

	protected Component makeResultsTable() {
		resultsTableModel = new DefaultTableModel();
		resultsTableModel.addColumn("Short name");
		resultsTableModel.addColumn("Title");
		resultsTableModel.addColumn("Subjects");
		resultsTableModel.addColumn("Identifier");
		resultsTableModel.addColumn("Publisher");

		// FIXME: Dummy data
		for (int i = 1; i < 6; i++) {
			resultsTableModel.addRow(new String[] { "AMIGA" + i,
					"Amiga test #" + i, "Interesting stars", "/dev/null",
					"Technical support" });
		}

		JTable resultsTable = new JTable(resultsTableModel);
		// resultsTable.setAutoCreateColumnsFromModel(true);
		resultsTable.setAutoCreateRowSorter(true);
		// resultsTable.createDefaultColumnsFromModel();
		resultsTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		return new JScrollPane(resultsTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Component makeSearchBox() {
		JPanel searchBox = new JPanel(new GridBagLayout());
		GridBagConstraints gbcLeft = new GridBagConstraints();
		GridBagConstraints gbcMiddle = new GridBagConstraints();
		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcLeft.gridx = 0;
		add(new JLabel("Registry:"), gbcLeft);
		gbcMiddle.anchor = GridBagConstraints.WEST;
		gbcMiddle.fill = GridBagConstraints.HORIZONTAL;
		gbcMiddle.gridx = 1;
		gbcMiddle.weightx = 0.1;

		URI[] registries = getRegistries().toArray(new URI[0]);
		registry = new JComboBox(registries);
		registry.setEditable(true);

		add(registry, gbcMiddle);

		add(new JLabel("Keywords:"), gbcLeft);
		keywords = new JTextField(40);
		keywords.setAction(coneSearch);
		add(keywords, gbcMiddle);

		gbcRight.gridx = 1;
		gbcRight.gridy = 2;
		gbcRight.anchor = GridBagConstraints.WEST;
		add(makeSearchButtons(), gbcRight);

		GridBagConstraints filler = new GridBagConstraints();
		filler.gridx = 3;
		filler.weightx = 1.0;
		filler.fill = GridBagConstraints.HORIZONTAL;
		add(new JPanel(), filler);

		return searchBox;

	}
	ConeSearch coneSearch = new ConeSearch();
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onDispose() {
		// TODO Auto-generated method stub
	}

}
