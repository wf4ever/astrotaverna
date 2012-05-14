package org.purl.wf4ever.astrotaverna.view.votable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.renderers.Renderer;
import net.sf.taverna.t2.renderers.RendererException;
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
	    
		
		return new JScrollPane(jTable);
	}

	@Override
	public String getType() {
		return "VOTable";
	}

}
