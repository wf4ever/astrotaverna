package org.purl.wf4ever.astrotaverna.vo;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class VOServiceIcon implements ActivityIconSPI {

	private static final String VO_32X16_PNG = "NGC_4414_16x16.png";
	public static ImageIcon voIcon = new ImageIcon(
			VOServicesPerspective.class.getResource(VO_32X16_PNG));

	@Override
	public int canProvideIconScore(Activity<?> activity) {
		if (activity instanceof RESTActivity) {
			RESTActivity restActivity = (RESTActivity) activity;
			if (isAstro(restActivity))
				;
			return ActivityIconSPI.DEFAULT_ICON + 100;
		}
		return ActivityIconSPI.NO_ICON;
	}

	public boolean isAstro(RESTActivity restActivity) {
		// TEST: Check this.. somehow!
		return true;
	}

	@Override
	public Icon getIcon(Activity<?> activity) {
		return voIcon;
	}

}
