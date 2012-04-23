package uk.org.taverna.astro.vo;

import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.ui.zaria.UIComponentFactorySPI;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

public class VOServicesComponentFactory implements UIComponentFactorySPI {

	@Override
	public UIComponentSPI getComponent() {
		return new VOServicesComponent();
	}

	@Override
	public ImageIcon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	@Override
	public String getName() {
		return "VO services";
	}

}
