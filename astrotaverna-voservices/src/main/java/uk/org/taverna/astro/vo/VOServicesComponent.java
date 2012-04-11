package uk.org.taverna.astro.vo;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import net.sf.taverna.t2.workbench.ui.zaria.UIComponentSPI;

public class VOServicesComponent extends JComponent implements UIComponentSPI {

	private static final long serialVersionUID = 1L;

	public VOServicesComponent() {
		add(new JLabel("VO services:"));
	}
	
	@Override
	public ImageIcon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	@Override
	public void onDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDispose() {
		// TODO Auto-generated method stub
	}

}
