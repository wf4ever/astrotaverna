package org.purl.wf4ever.astrotaverna.view.votable;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
		String asString = (String) referenceService.renderIdentifier(reference, String.class, null);
		// TODO: Also recognize FITS and other formats from 
		// http://www.star.bris.ac.uk/~mbt/stil/sun252/tableBuilders.html
		return asString.contains("http://www.ivoa.net/xml/VOTable");		
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
	    JTable jTable;
//	    StarTableModel model = new StarTableModel(starTable, false);
//	    jTable = new JTable(model);
//		jTable.setAutoCreateColumnsFromModel(true);
//		jTable.setAutoCreateRowSorter(true);
		jTable = new StarJTable(starTable, false);
		
		return new JScrollPane(jTable);
	}

	@Override
	public String getType() {
		return "VOTable";
	}

}
