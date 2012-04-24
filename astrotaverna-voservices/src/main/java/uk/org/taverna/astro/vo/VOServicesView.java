package uk.org.taverna.astro.vo;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.slap.v0.SimpleLineAccess;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

import org.apache.log4j.Logger;

import uk.org.taverna.astro.vo.utils.ModelIterator;
import uk.org.taverna.astro.vorepo.VORepository.Status;

public class VOServicesView extends JPanel implements UIComponentSPI {
	public class RegistryChange implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox combo = (JComboBox) e.getSource();
			getController().changeEndpoint((String) combo.getSelectedItem());
		}
	}

	public class AddToWorkflow extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddToWorkflow() {
			super("Add to workflow");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getController().addToWorkflow();
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
	
	public class SLASearchAction extends SearchAction {
		private static final long serialVersionUID = 1L;

		public SLASearchAction() {
			super("SLA Search");
			this.searchType = SimpleLineAccess.class;
		}
	}

	static Logger logger = Logger.getLogger(VOServicesView.class);
	private static final int RESOURCE_COLUMN = 0;
	private static final long serialVersionUID = 1L;
	// Actions
	private ConeSearchAction coneSearch = new ConeSearchAction();
	private VOServicesController controller;
	private JTextField keywords;
	private VOServicesModel model;

	private JComboBox registry;
	private JSplitPane results;
	private JPanel resultsDetails;

	private JTable resultsTable;

	private DefaultTableModel resultsTableModel;

	private SIASearchAction siaSearchAction = new SIASearchAction();
//	private SLASearchAction slaSearchAction = new SLASearchAction();
	private SSASearchAction ssaSearchAction = new SSASearchAction();

	// SWing stuff
	private JLabel status;
	private VOServiceDetails serviceDetails;
	private AddToWorkflow addToWorkflow;

	public VOServicesView() {
		initialize();
	}

	public void setSearch(String search) {
		keywords.setText(search);
	}

	public String getSearch() {
		return keywords.getText();
	}

	public void clearResults() {
		int rows = resultsTableModel.getRowCount();
		while (rows > 0) {
			resultsTableModel.removeRow(--rows);
		}

		// results.validate();
	}

	public VOServicesController getController() {
		if (controller == null) {
			controller = new VOServicesController();
			controller.setView(this);
			controller.setModel(getModel());
		}
		return controller;
	}

	@Override
	public ImageIcon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	public VOServicesModel getModel() {
		if (model == null) {
			model = new VOServicesModel();
			model.setView(this);
			model.setController(getController());
		}
		return model;
	}

	protected Service getServiceAtRow(int row) {
		if (row < 0) {
			return null;
		}
		return ((Service) resultsTableModel.getValueAt(row, RESOURCE_COLUMN));
	}

	protected Service getTableSelection() {
		return getServiceAtRow(resultsTable.getSelectionModel()
				.getMinSelectionIndex());
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
		getController().checkEndpoint();
		updateDetails();
		updateServices();
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
		resultsDetails.removeAll();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		serviceDetails = new VOServiceDetails();
		resultsDetails.add(new JScrollPane(serviceDetails,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gbc);

		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		JPanel buttonPanel = new JPanel();
		addToWorkflow = new AddToWorkflow();
		buttonPanel.add(new JButton(addToWorkflow), gbc);
		resultsDetails.add(buttonPanel, gbc);

		// gbc.weighty = 0.1;
		// JPanel filler = new JPanel();
		// resultsDetails.add(filler, gbc); // filler
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

		String[] registries = getEndpoints();
		registry = new JComboBox(registries);
		registry.setEditable(true);
		registry.addActionListener(new RegistryChange());

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

	protected String[] getEndpoints() {
		List<String> endpoints = new ArrayList<String>();
		for (URI registry : getModel().getEndpoints()) {
			endpoints.add(registry.toString());
		}
		return endpoints.toArray(new String[endpoints.size()]);
	}

	protected JPanel makeSearchButtons() {
		JPanel searchButtons = new JPanel(new FlowLayout());

		searchButtons.add(new JButton(coneSearch));
		searchButtons.add(new JButton(siaSearchAction));
		// Disabled as it generally gives 0 results
		//searchButtons.add(new JButton(slaSearchAction));
		searchButtons.add(new JButton(ssaSearchAction));
		return searchButtons;
	}

	@Override
	public void onDisplay() {
	}

	@Override
	public void onDispose() {
		getController().cancelTaskIfNeeded();
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	protected void setTableSelection(Service selectedService) {
		if (selectedService == getTableSelection()) {
			// Already there - perhaps selection was done in table?
			return;
		}
		for (int row = -1; row < resultsTableModel.getRowCount(); row++) {
			if (getServiceAtRow(row) == selectedService) {
				resultsTable.getSelectionModel().setSelectionInterval(row, row);
				break;
			}
		}
	}

	public void statusCancelled(CancellationException ex) {
		status.setText("<html><body><font color='#dd2222'>"
				+ "Cancelled search" + "</font><body></html>");
	}

	public void statusWarn(String msg) {
		status.setText("<html><body><font color='#dd2222'>" + msg
				+ "</font><body></html>");
	}

	public void statusFailed(Exception ex) {
		statusWarn("Search failed: " + ex.getLocalizedMessage());
	}

	public void statusFoundResults(int size) {
		status.setText(String
				.format("%d results for %s: %s", size, getModel()
						.getCurrentSearchType().getSimpleName(), getModel()
						.getSearch()));
	}

	public void statusInterrupted(InterruptedException ex) {
		statusWarn("Search interrupted: " + ex.getLocalizedMessage());
	}

	public void statusSearching(Class<? extends Capability> searchType,
			String search) {
		status.setText(String.format(
				"<html><body>Searching for %s: <b>%s</b> ...</body></html>",
				searchType.getSimpleName(), search));
	}

	protected void updateDetails() {
		Service service = getModel().getSelectedService();
		serviceDetails.setService(service);
		if (service == null) {
			addToWorkflow.setEnabled(false);
			return;
		}
		addToWorkflow.setEnabled(true);
	}


	public void updateSelection() {
		updateDetails();
		setTableSelection(getModel().getSelectedService());
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

	public void statusInvalidEndpoint(Exception e) {
		statusWarn(String.format("Invalid registry %s %s", getModel()
				.getEndpoint(), e.getLocalizedMessage()));
	}

	public void statusEndpointOK() {
		status.setText("Endpoint OK");
	}

	public void statusEndpointStatus(Status endpointStatus) {
		statusWarn(String.format("Endpoint %s: %s", getModel().getEndpoint(),
				endpointStatus));
	}

	public void updateEndpoint() {
		URI endpoint = getModel().getEndpoint();
		String endpointStr = endpoint.toString();
		// Check if it's already there..
		for (String item : new ModelIterator<String>(registry.getModel())) {
			if (item.equals(endpointStr)) {
				registry.setSelectedItem(item);
				return;
			}
		}
		registry.addItem(endpointStr);
		registry.setSelectedItem(endpointStr);
	}

	public void statusEndpointChecking() {
		status.setText("Checking endpoint...");
	}

}
