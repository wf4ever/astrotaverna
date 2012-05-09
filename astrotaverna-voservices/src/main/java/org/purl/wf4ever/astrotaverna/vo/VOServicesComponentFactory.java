package org.purl.wf4ever.astrotaverna.vo;

import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.ui.zaria.UIComponentFactorySPI;
import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

public class VOServicesComponentFactory implements UIComponentFactorySPI {

	@Override
	public UIComponentSPI getComponent() {
		return new VOServicesView();
	}

	@Override
	public ImageIcon getIcon() {
		return VOServiceIcon.voIcon;
	}

	@Override
	public String getName() {
		return "VO services";
	}

}
