package org.purl.wf4ever.astrotaverna.view.votable;


import javax.swing.JComponent;
import javax.swing.JTextArea;

import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.renderers.Renderer;
import net.sf.taverna.t2.renderers.RendererException;

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
		return asString.contains("http://www.ivoa.net/xml/VOTable");		
	}

	@Override
	public JComponent getComponent(ReferenceService referenceService,
			T2Reference reference) throws RendererException {
		String asString = (String) referenceService.renderIdentifier(reference, String.class, null);
		return new JTextArea(asString);
	}

	@Override
	public String getType() {
		return "VOTable";
	}

}
