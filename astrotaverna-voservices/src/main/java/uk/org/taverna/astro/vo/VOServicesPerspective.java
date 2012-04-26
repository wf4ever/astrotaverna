package uk.org.taverna.astro.vo;

import java.io.InputStream;

import javax.swing.ImageIcon;

import net.sf.taverna.t2.ui.perspectives.AbstractPerspective;
import net.sf.taverna.t2.workbench.ui.zaria.PerspectiveSPI;

public class VOServicesPerspective extends AbstractPerspective implements
		PerspectiveSPI {

	

	@Override
	public String getText() {
		return "VO services";
	}

	@Override
	public ImageIcon getButtonIcon() {
		return VOServiceIcon.voIcon;
	}

	@Override
	protected InputStream getLayoutResourceStream() {
		return getClass().getResourceAsStream("voservices-perspective.xml");
	}

	@Override
	public int positionHint() {
		// Just before BioCatalogue perspective
		return 38;
	}

}
