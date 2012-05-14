package org.purl.wf4ever.astrotaverna.view.votable;

import java.awt.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.renderers.Renderer;
import net.sf.taverna.t2.renderers.RendererException;

import org.purl.wf4ever.astrotaverna.vo.utils.HTMLPane;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.table.gui.StarJTable;
import uk.ac.starlink.util.ByteArrayDataSource;

public class VOTableRenderer implements Renderer {

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
	public JComponent getComponent(ReferenceService referenceService,
			T2Reference reference) throws RendererException {
		byte[] bytes = (byte[]) referenceService.renderIdentifier(reference, byte[].class, null);		
		StarTableFactory factory = new StarTableFactory();
		ByteArrayDataSource dataSource = new ByteArrayDataSource("votable", bytes);
	    StarTable starTable;
		try {
			starTable = factory.makeStarTable(dataSource);
		} catch (TableFormatException e) {
			throw new RendererException("Not votable format", e);
		} catch (IOException e) {
			throw new RendererException(e.getLocalizedMessage(), e);
		}
		StarJTable jTable;
	    jTable = new StarJTable(starTable, false);
//	    StarTableModel model = new StarTableModel(starTable, false);
//	    jTable = new JTable(model);	
//		jTable.setAutoCreateColumnsFromModel(true);
		jTable.setAutoCreateRowSorter(true);
		jTable.configureColumnWidths(400, 100);
	    
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("VOTable", new JScrollPane(jTable));
		tabs.add("Metadata", new JScrollPane(makeMetaData(starTable)));
		return new JScrollPane(tabs);
	}

	@SuppressWarnings("unchecked")
	protected Component makeMetaData(StarTable starTable) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		
		sb.append("<h2>Columns</h2>");
		for (int i=0; i<starTable.getColumnCount(); i++) {
			ColumnInfo colInfo = starTable.getColumnInfo(i);			
			sb.append(HTMLPane.format("<div style='font-size: 110%%;'>%s</div>", colInfo.getName()));
			sb.append(HTMLPane.format("<div><em>%s</em></div>", colInfo.getDescription()));
			
			sb.append(HTMLPane.format("<div><strong>Unit</strong> <span>%s</span></div>", colInfo.getUnitString()));			
			sb.append(HTMLPane.format("<div><strong>UCD</strong> <span>%s</span></div>", colInfo.getUCD()));
			sb.append(HTMLPane.format("<div><strong>Utype</strong> <span>%s</span></div>", colInfo.getUtype()));
			sb.append(HTMLPane.format("<div><strong>Element size</strong> <span>%s</span></div>", colInfo.getElementSize()));
			for (DescribedValue value : (List<DescribedValue>)colInfo.getAuxData()) {
				sb.append(HTMLPane.format("<div><strong>%s</strong> <span>%s</span></div>", value.getInfo().getName(), value.getValueAsString(150)));
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
