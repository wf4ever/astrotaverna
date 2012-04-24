package uk.org.taverna.astro.vo;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.text.SimpleDateFormat;
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
import net.ivoa.xml.conesearch.v1.Query;
import net.ivoa.xml.sia.v1.ImageSize;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.sia.v1.SkySize;
import net.ivoa.xml.slap.v0.SimpleLineAccess;
import net.ivoa.xml.ssa.v0.DataSource;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.vodataservice.v1.HTTPQueryType;
import net.ivoa.xml.vodataservice.v1.InputParam;
import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Contact;
import net.ivoa.xml.voresource.v1.Content;
import net.ivoa.xml.voresource.v1.Creator;
import net.ivoa.xml.voresource.v1.Curation;
import net.ivoa.xml.voresource.v1.Date;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.ResourceName;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.Source;
import net.ivoa.xml.voresource.v1.Type;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

import org.apache.log4j.Logger;

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

	private SSASearchAction ssaSearchAction = new SSASearchAction();

	// SWing stuff
	private JLabel status;
	private HTMLPane htmlPane;
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
		htmlPane = new HTMLPane();
		resultsDetails.add(new JScrollPane(htmlPane,
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
		if (service == null) {
			htmlPane.reset();
			addToWorkflow.setEnabled(false);
			return;
		}

		StringBuffer message = new StringBuffer();
		appendFormat(message, "<html><body><h2>%s: %s</h2>",				
				service.getShortName(), service.getTitle());

		Content content = service.getContent();
		if (content != null) {
			
			message.append("<div><em>");
			for (Type t : content.getType()) {
				message.append(t.name());
				message.append(" ");
			}
			message.append("</em></div>");
			appendFormat(message, "<div>%s</div>", content.getDescription());
			
			message.append("<dl>");
			Source source = content.getSource();
			if (source != null) {
				message.append("<dt>Source</dt>");
				appendFormat(message, "<dd>%s <code>%s</code></dd>",
						source.getValue(), source.getFormat());
			}
			if (!content.getSubject().isEmpty()) {
				message.append("<dt>Subjects</dt>");
				for (String s : content.getSubject()) {
					appendFormat(message, "<dd>%s</dd>", s);					
				}
			}
		}
		message.append("</dl>");


		
		for (Capability c : service.getCapability()) {
			boolean seenCap = false;
			for (Interface i : c.getInterface()) {
				if (!(i instanceof ParamHTTP)) {
					continue;
					// TODO: Handle WebService interface?
				}
				if (!seenCap) {
					seenCap = true;
					message.append("<h3>Service</h3>");
					appendFormat(message, "<div><code>%s</code></div>", c.getStandardID());
					appendFormat(message, "<div>%s</div>", c.getDescription());
					if (c instanceof ConeSearch) {
						ConeSearch cone = (ConeSearch) c;
						message.append("<dl>");
						if (cone.isVerbosity()) {
							message.append("<dt>Verbose</dt>");
						}
						appendFormat(message,
								"<dt>Maximum records</dt><dd>%s</dd>",
								cone.getMaxRecords());

						appendFormat(message,
								"<dt>Maximum search radius</dt><dd>%s</dd>",
								cone.getMaxSR());

						if (cone.getTestQuery() != null) {
							message.append("<dt>Test query</dt><dd>");
							message.append("<dl>");
							Query tq = cone.getTestQuery();
							appendFormat(message,
									"<dt>Catalogue</dt><dd>%s</dd>",
									tq.getCatalog());
							appendFormat(message, "<dt>SR</dt><dd>%s</dd>",
									tq.getSr());
							appendFormat(message, "<dt>DEC</dt><dd>%s</dd>",
									tq.getDec());
							appendFormat(message, "<dt>RA</dt><dd>%s</dd>",
									tq.getRa());
							appendFormat(message, "<dt>Extras</dt><dd>%s</dd>",
									tq.getExtras());
							appendFormat(message, "<dt>Verb</dt><dd>%s</dd>",
									tq.getVerb());
							message.append("</dl>");
							message.append("</dd>");
						}

						message.append("</dl>");

					}

					if (c instanceof SimpleLineAccess) {
						SimpleLineAccess sla = (SimpleLineAccess) c;
						message.append("<dl>");

						appendFormat(message,
								"<dt>Maximum records</dt><dd>%s</dd>",
								sla.getMaxRecords());
						appendFormat(message, "<dt>Source</dt><dd>%s</dd>",
								sla.getDataSource());

						if (sla.getTestQuery() != null) {
							message.append("<dt>Test query</dt><dd>");
							message.append("<dl>");
							net.ivoa.xml.slap.v0.Query tq = sla.getTestQuery();
							appendFormat(message,
									"<dt>Wavelength</dt><dd>%s - %s</dd>",
									tq.getWavelength().getMinWavelength(), tq.getWavelength().getMaxWavelength());
							appendFormat(message,
									"<dt>Query data cmd</dt><dd>%s</dd>",
									tq.getQueryDataCmd());
							message.append("</dl>");
							message.append("</dd>");
						}
						message.append("</dl>");
					}

					if (c instanceof SimpleImageAccess) {
						SimpleImageAccess sia = (SimpleImageAccess) c;
						message.append("<dl>");

						appendFormat(message,
								"<dt>Maximum records</dt><dd>%s</dd>",
								sia.getMaxRecords());

						appendFormat(message,
								"<dt>Image service type</dt><dd>%s</dd>",
								sia.getImageServiceType());
						if (sia.getMaxQueryRegionSize() != null) {
							SkySize size = sia.getMaxQueryRegionSize();
							message.append("<dt>Maximum query region size</dt>");
							appendFormat(message, "<dd>Lat: %s, Long: %s</dd>",
									size.getLat(), size.getLong());
						}
						if (sia.getMaxImageExtent() != null) {
							SkySize size = sia.getMaxImageExtent();
							message.append("<dt>Maximum image extent</dt>");
							appendFormat(message, "<dd>Lat: %s, Long: %s</dd>",
									size.getLat(), size.getLong());
						}
						if (sia.getMaxImageSize() != null) {
							ImageSize size = sia.getMaxImageSize();
							message.append("<dt>Maximum image size</dt>");
							appendFormat(message, "<dd>Lat: %s, Long: %s</dd>",
									size.getLat(), size.getLong());
						}

						appendFormat(message,
								"<dt>Maximum file size</dt><dd>%s</dd>",
								sia.getMaxFileSize());

						if (sia.getTestQuery() != null) {
							message.append("<dt>Test query</dt><dd>");
							message.append("<dl>");
							net.ivoa.xml.sia.v1.Query tq = sia.getTestQuery();
							appendFormat(message, "<dt>POS</dt><dd>Lat: %s, Long: %s</dd>",
									tq.getPos().getLat(), tq.getPos().getLong());
							appendFormat(message, "<dt>SIZE</dt><dd>Lat: %s, Long: %s</dd>",
									tq.getSize().getLat(), tq.getSize().getLong());
							appendFormat(message, "<dt>Extras</dt><dd>%s</dd>",
									tq.getExtras());
							appendFormat(message, "<dt>Verb</dt><dd>%s</dd>",
									tq.getVerb());
							message.append("</dl>");
							message.append("</dd>");
						}
						message.append("</dl>");
					}
					if (c instanceof SimpleSpectralAccess) {
						SimpleSpectralAccess spa = (SimpleSpectralAccess) c;
						message.append("<dl>");

						appendFormat(message,
								"<dt>Maximum records</dt><dd>%s</dd>",
								spa.getMaxRecords());

						appendFormat(message,
								"<dt>Maximum search radius</dt><dd>%s</dd>",
								spa.getMaxSearchRadius());
						appendFormat(message,
								"<dt>Maximum aperture</dt><dd>%s</dd>",
								spa.getMaxAperture());

						appendFormat(message,
								"<dt>Maximum file size</dt><dd>%s</dd>",
								spa.getMaxFileSize());
						if (!spa.getDataSource().isEmpty()) {
							message.append("<dt>Sources</dt>");
							for (DataSource source : spa.getDataSource()) {
								appendFormat(message, "<dd>%s</dd>", source);
							}
						}

						if (!spa.getSupportedFrame().isEmpty()) {
							message.append("<dt>Supported frames</dt>");
							for (String frame : spa.getSupportedFrame()) {
								appendFormat(message, "<dd>%s</dd>", frame);
							}
						}

						if (spa.getTestQuery() != null) {
							message.append("<dt>Test query</dt><dd>");
							message.append("<dl>");
							net.ivoa.xml.ssa.v0.Query tq = spa.getTestQuery();
							appendFormat(message, "<dt>POS</dt><dd>Lat: %s, Long: %s</dd>",
									tq.getPos().getLat(), tq.getPos().getLong());
							appendFormat(message, "<dt>SIZE</dt><dd>%s</dd>",
									tq.getSize());
							appendFormat(message,
									"<dt>Query data cmd</dt><dd>%s</dd>",
									tq.getQueryDataCmd());
							message.append("</dl>");
							message.append("</dd>");
						}
						message.append("</dl>");
					}

				}

				ParamHTTP paramHTTP = (ParamHTTP) i;

				HTTPQueryType queryType = paramHTTP.getQueryType();
				if (queryType == null) {
					queryType = HTTPQueryType.GET;
				}
				for (AccessURL accessURL : i.getAccessURL()) {
					message.append(String.format("<p><code>%s %s</code></p>", queryType,
							accessURL.getValue().trim()));
				}
				appendFormat(message, "<div><em>Version: %s</em></div>", paramHTTP.getVersion());
				appendFormat(message,
						"<div>Result type: <code>%s</code></div>",
						paramHTTP.getResultType());
				if (paramHTTP.getParam() != null
						&& !paramHTTP.getParam().isEmpty()) {
					message.append("<h4>Input parameters</h4><dl>");
					for (InputParam param : paramHTTP.getParam()) {
						message.append("<dt>");
						message.append(param.getName());
						message.append(" (");
						message.append(param.getUse());
						message.append(" )");
						message.append("</dt><dd>");
						if (param.getUnit() != null) {
							message.append(param.getUnit());
						}
						if (param.getDataType() != null) {
							message.append(" <code>");
							message.append(param.getDataType().getValue());
							message.append(" [");
							message.append(param.getDataType().getArraysize());
							message.append("]");
							message.append("</code>");
						}
						message.append("</dd>");
						appendFormat(message, "<dd><em>%s</em></dd>",
								param.getDescription());
					}
					message.append("</dl>");
				}
			}

		}
		message.append("<h3>More information</h3><dl>");
		appendFormat(message, "<dt>Identifier</dt><dd>%s</dd>",
				service.getIdentifier());
		appendFormat(message, "<dt>Status</dt><dd>%s</dd>", service.getStatus());
		if (service.getUpdated() != null) {
			appendFormat(
					message,
					"<dt>Updated</dt><dd>%s</dd>",
					new SimpleDateFormat().format(service.getUpdated()
							.toGregorianCalendar().getTime()));
		}
		Curation curation = service.getCuration();
		if (curation != null) {
			appendFormat(message, "<dt>Publisher</dt><dd>%s</dd>", curation
					.getPublisher().getValue());
			if (!curation.getContributor().isEmpty()) {
				message.append("<dt>Contributor</dt>");
				for (ResourceName contrib : curation.getContributor()) {
					appendFormat(message, "<dd>%s</dd>", contrib.getValue());
				}
			}
			if (!curation.getCreator().isEmpty()) {
				message.append("<dt>Creator</dt>");
				for (Creator creator : curation.getCreator()) {
					appendFormat(message, "<dd>%s</dd>", creator.getName()
							.getValue());
				}
			}
			if (!curation.getCreator().isEmpty()) {
				message.append("<dt>Contact</dt>");
				for (Contact contact : curation.getContact()) {
					appendFormat(message, "<dd>%s &lt;<a href='mailto:%s'>%s</a>&gt; "
							+ "<br><a href='tel:%s'>%s</a><br>%s</dd>", contact
							.getName().getValue(), contact.getEmail(),
							contact.getEmail(), contact.getTelephone(),
							contact.getTelephone(), contact.getAddress());
				}
			}
			appendFormat(message, "<dt>Version</dt><dd>%s</dd>",
					curation.getVersion());
			if (!curation.getDate().isEmpty()) {
				message.append("<dt>Date</dt>");
				for (Date date : curation.getDate()) {
					appendFormat(message, "<dd>%s %s</dd>", date.getValue(),
							date.getRole());
				}
			}
		}
		appendFormat(message, "<dt>Reference</dt><dd><a href='%s'>%s</a></dd>",
				service.getContent().getReferenceURL(), service.getContent()
						.getReferenceURL());

		message.append("</dl></body></html>");

		htmlPane.setText(message.toString());
		addToWorkflow.setEnabled(true);

	}

	private void appendFormat(StringBuffer message, String formatStr,
			Object... args) {
		boolean foundAny = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				args[i] = "";
			} else {
				foundAny = true;
			}
		}
		if (!foundAny) {
			return;
		}
		message.append(String.format(formatStr, args));
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
