package org.purl.wf4ever.astrotaverna.pdl.ui.serviceprovider;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class PDLServiceIcon implements ActivityIconSPI {

	private static Icon icon;

	public int canProvideIconScore(Activity<?> activity) {
		if ((activity instanceof ValidationPDLClientActivity)) {
			return DEFAULT_ICON;
		}
		return NO_ICON;
	}

	public Icon getIcon(Activity<?> activity) {
		return getIcon();
	}

	public static Icon getIcon() {
		if (icon == null) {
			icon = new ImageIcon(
					PDLServiceIcon.class
							.getResource("/NGC_4414_16x16.png"));
		}
		return icon;
	}

}
