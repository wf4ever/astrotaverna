package org.purl.wf4ever.astrotaverna.view.votable;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.taverna.t2.reference.ExternalReferenceSPI;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.ReferenceSet;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.reference.impl.external.file.FileReference;
import net.sf.taverna.t2.renderers.Renderer;
import net.sf.taverna.t2.renderers.RendererException;

import org.apache.log4j.Logger;
import org.purl.wf4ever.astrotaverna.samp.TavernaSampConnection;
import org.purl.wf4ever.astrotaverna.vo.utils.HTMLPane;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.table.gui.StarJTable;
import uk.ac.starlink.util.ByteArrayDataSource;

public class VOTableRenderer implements Renderer {

	private TavernaSampConnection sampConn = TavernaSampConnection
			.getInstance();

	private static Logger logger = Logger.getLogger(VOTableRenderer.class);

	protected List<String> PREDICTORS = Arrays.asList(
			"http://www.ivoa.net/xml/VOTable",
			"http://vizier.u-strasbg.fr/VOTable",
			"http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd",
			"http://www.ivoa.net/xml/VOTable/v1.0",
			"http://www.ivoa.net/xml/VOTable/v1.2",
			"http://us-vo.org/xml/VOTable.dtd");

	@Override
	public boolean canHandle(String mimeType) {
		return "application/x-votable+xml".equals(mimeType);
	}

	@Override
	public boolean canHandle(ReferenceService referenceService,
			T2Reference reference, String mimeType) throws RendererException {
		if (reference.containsErrors() || reference.getDepth() > 0) {
			return false;
		}
		if (canHandle(mimeType)) {
			return true;
		}
		String asString = (String) referenceService.renderIdentifier(reference,
				String.class, null);
		for (String predictor : PREDICTORS) {
			if (asString.contains(predictor)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public JComponent getComponent(final ReferenceService referenceService,
			final T2Reference reference) throws RendererException {
		final byte[] bytes = (byte[]) referenceService.renderIdentifier(
				reference, byte[].class, null);
		StarTableFactory factory = new StarTableFactory();
		ByteArrayDataSource dataSource = new ByteArrayDataSource("votable",
				bytes);
		StarTable starTable;
		try {
			starTable = factory.makeStarTable(dataSource);
		} catch (TableFormatException e) {
			throw new RendererException("Not votable format", e);
		} catch (IOException e) {
			throw new RendererException(e.getLocalizedMessage(), e);
		}
		final StarJTable jTable;
		jTable = new StarJTable(starTable, false);
		// StarTableModel model = new StarTableModel(starTable, false);
		// jTable = new JTable(model);
		// jTable.setAutoCreateColumnsFromModel(true);
		jTable.setAutoCreateRowSorter(true);
		jTable.configureColumnWidths(400, 100);
		ListSelectionListener sampListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				highlightRowSamp(reference, jTable);
			}

			
		};
		jTable.getSelectionModel().addListSelectionListener(sampListener);

		JTabbedPane tabs = new JTabbedPane();
		tabs.add("VOTable", new JScrollPane(jTable));
		tabs.add("Metadata", new JScrollPane(makeMetaData(starTable)));
		// TODO: Is it possible to get the port name from the renderer instead 
		// of starTable.getName()?
		tabs.add("SAMP", makeSampPanel(reference, referenceService, jTable, starTable.getName()));

		return tabs;
	}
	
	private void highlightRowSamp(T2Reference reference, StarJTable jTable) {
		try {
			URI uri = sentTables.get(reference);
			if (uri == null) {
				// SAMP does not know about this one yet
				return;
			}			
			sampConn.highlightRow(uri, jTable
					.convertRowIndexToModel(jTable.getSelectedRow()), reference.toUri().toASCIIString());
		} catch (IOException e1) {
			logger.warn("Could not send selection to SAMP", e1);
		}
	}
	
	private static WeakHashMap<T2Reference, URI> sentTables = new WeakHashMap<T2Reference, URI>();  
	
	private JPanel makeSampPanel(final T2Reference reference,
			final ReferenceService referenceService, final StarJTable jTable, final String name) {
		final JPanel panel = new JPanel();

		panel.add(new JButton(new AbstractAction("Send VOTable to SAMP") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent ename) {
				try {
					File file = getFileFromReference(reference,
							referenceService);
					URI uri = file.toURI();
					
					sampConn.sendVOTable(uri, reference.toUri().toASCIIString(), name);
					sentTables.put(reference, uri);
					highlightRowSamp(reference, jTable);
					sampConn.registerForSelection(reference.toUri().toASCIIString(), null);
					
					JOptionPane.showMessageDialog(panel, "Sent over SAMP");
				} catch (IOException e1) {
					logger.warn("Could not send table to SAMP", e1);
					JOptionPane.showMessageDialog(
							panel,
							"Could not send table to SAMP.\n"
									+ e1.getLocalizedMessage());
				}
			}
		}));
		return panel;
	}

	protected File getFileFromReference(T2Reference reference,
			ReferenceService referenceService) throws IOException {
		FileReference fileRef = getReference(reference, FileReference.class,
				referenceService);
		File file = fileRef.getFile();
		if (!file.isFile()) {
			throw new IOException("File no longer exists: " + file);
		}
		return file;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T extends ExternalReferenceSPI> T getReference(
			T2Reference reference, Class<T> externalReferenceType,
			ReferenceService referenceService) {
		Set types = new HashSet();
		types.add(FileReference.class);
		ReferenceSet rs = (ReferenceSet) referenceService.resolveIdentifier(
				reference, types, null);
		for (ExternalReferenceSPI ref : rs.getExternalReferences()) {
			if (externalReferenceType.isInstance(ref)) {
				return (T) ref;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Component makeMetaData(StarTable starTable) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");

		sb.append("<h2>Columns</h2>");
		for (int i = 0; i < starTable.getColumnCount(); i++) {
			ColumnInfo colInfo = starTable.getColumnInfo(i);
			sb.append(HTMLPane.format(
					"<div style='font-size: 110%%;'>%s</div>",
					colInfo.getName()));
			sb.append(HTMLPane.format("<div><em>%s</em></div>",
					colInfo.getDescription()));

			sb.append(HTMLPane.format(
					"<div><strong>Unit</strong> <span>%s</span></div>",
					colInfo.getUnitString()));
			sb.append(HTMLPane.format(
					"<div><strong>UCD</strong> <span>%s</span></div>",
					colInfo.getUCD()));
			sb.append(HTMLPane.format(
					"<div><strong>Utype</strong> <span>%s</span></div>",
					colInfo.getUtype()));
			sb.append(HTMLPane.format(
					"<div><strong>Element size</strong> <span>%s</span></div>",
					colInfo.getElementSize()));
			for (DescribedValue value : (List<DescribedValue>) colInfo
					.getAuxData()) {
				sb.append(HTMLPane.format(
						"<div><strong>%s</strong> <span>%s</span></div>", value
								.getInfo().getName(), value
								.getValueAsString(150)));
			}
			sb.append("<p></p>");

		}

		sb.append("</body></html>");
		return new HTMLPane(sb.toString());
	}

	@Override
	public String getType() {
		return "VOTable";
	}

}
