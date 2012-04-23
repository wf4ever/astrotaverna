package uk.org.taverna.astro.vo;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.concurrent.CancellationException;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

import org.apache.log4j.Logger;

public class VOServicesComponent extends JPanel implements UIComponentSPI {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VOServicesComponent.class);

	private static final int RESOURCE_COLUMN = 0;

	private VOServicesModel model;
	private VOServicesController controller;

	// SWing stuff
	private JLabel status;
	private JTextField keywords;
	@SuppressWarnings("rawtypes")
	private JComboBox registry;
	private JSplitPane results;
	private JPanel resultsDetails;
	private JTable resultsTable;
	private DefaultTableModel resultsTableModel;

	// Actions
	private ConeSearchAction coneSearch = new ConeSearchAction();
	private SIASearchAction siaSearchAction = new SIASearchAction();
	private SSASearchAction ssaSearchAction = new SSASearchAction();

	public VOServicesComponent() {
		initialize();
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
		getController().cancelSearchTaskIfNeeded();
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
		add(new JPanel(), gbc);// filler

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
						Service tableSelection = getTableSelection();
						getController().selectService(tableSelection);
					}
				});

		return new JScrollPane(resultsTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	protected Service getTableSelection() {
		return getServiceAtRow(resultsTable.getSelectionModel()
				.getMinSelectionIndex());
	}

	protected Service getServiceAtRow(int row) {
		if (row < 0) {
			return null;
		}
		return ((Service) resultsTableModel.getValueAt(row,
				RESOURCE_COLUMN));
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

		URI[] registries = getModel().getRegistries().toArray(new URI[0]);
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

	protected JPanel makeSearchButtons() {
		JPanel searchButtons = new JPanel(new FlowLayout());

		searchButtons.add(new JButton(coneSearch));
		searchButtons.add(new JButton(siaSearchAction));
		searchButtons.add(new JButton(ssaSearchAction));
		return searchButtons;
	}

	public void updateSelection() {
		updateDetails();
		setTableSelection(getModel().getSelectedService());
	}

	protected void setTableSelection(Service selectedService) {
		if (selectedService == getTableSelection()) {
			// Already there - perhaps selection was done in table?
			return;
		}		
		for (int row=-1; row < resultsTableModel.getRowCount(); row++) {		
			if (getServiceAtRow(row) == selectedService) {
				resultsTable.getSelectionModel().setSelectionInterval(row, row);
				break;
			}
		}
	}

	protected void updateDetails() {
		resultsDetails.removeAll();
		Service service = getModel().getSelectedService();
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
		// JPanel filler = new JPanel();
		// resultsDetails.add(filler, gbc); // filler
		results.validate();
	}

	public VOServicesModel getModel() {
		if (model == null) {
			model = new VOServicesModel();
			model.setView(this);
			model.setController(getController());
		}
		return model;
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	public VOServicesController getController() {
		if (controller == null) {
			controller = new VOServicesController();
			controller.setView(this);
			controller.setModel(getModel());
		}
		return controller;
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public class AddToWorkflow extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private final Service service;

		public AddToWorkflow(Service service) {
			super(String.format("Add %s to workflow", service.getShortName()));
			this.service = service;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getController().addToWorkflow(service);
		}
	}

	public class ConeSearchAction extends SearchAction {
		private static final long serialVersionUID = 1L;

		public ConeSearchAction() {
			super("Cone Search");
			this.searchType = ConeSearch.class;
		}
	}

	public class SearchAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		protected Class<? extends Capability> searchType;

		public SearchAction(String label) {
			super(label);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String search = keywords.getText();
			getController().search(searchType, search);

		}
	}

	public class SIASearchAction extends SearchAction {
		private static final long serialVersionUID = 1L;

		public SIASearchAction() {
			super("SIA Search");
			this.searchType = SimpleImageAccess.class;
		}
	}

	public class SSASearchAction extends SearchAction {
		private static final long serialVersionUID = 1L;

		public SSASearchAction() {
			super("SSA Search");
			this.searchType = SimpleSpectralAccess.class;
		}
	}

	public void statusSearching(Class<? extends Capability> searchType,
			String search) {
		status.setText(String.format("%s: %s", searchType.getSimpleName(),
				search));
	}

	public void clearResults() {
		int rows = resultsTableModel.getRowCount();
		while (rows > 0) {
			resultsTableModel.removeRow(--rows);
		}
		resultsDetails.removeAll();
		results.validate();
	}

	public void statusCancelled(CancellationException ex) {
		status.setText("<html><body><font color='#dd2222'>"
				+ "Cancelled search" + "</font><body></html>");
	}

	public void statusInterrupted(InterruptedException ex) {
		status.setText("<html><body><font color='#dd2222'>"
				+ "Search interrupted: " + ex.getLocalizedMessage()
				+ "</font><body></html>");
	}

	public void statusFailed(Exception ex) {
		status.setText("<html><body><font color='#dd2222'>" + "Search failed: "
				+ ex.getLocalizedMessage() + "</font><body></html>");
	}

	public void statusFoundResults(int size) {
		status.setText(String
				.format("%d results for %s: %s", size, getModel()
						.getCurrentSearchType().getSimpleName(), getModel()
						.getSearch()));
	}

	public void updateServices() {
		for (Service s : getModel().getServices()) {
			String shortName = s.getShortName();
			String title = s.getTitle();
			String subjects = s.getContent().getSubject().toString();
			String identifier = s.getIdentifier();
			String publisher = s.getCuration().getPublisher().getValue();
			resultsTableModel.addRow(new Object[] { s, shortName, title,
					subjects, identifier, publisher });
		}
	}

}
