package uk.org.taverna.astro.vo;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

public class VOServiceIcon implements ActivityIconSPI {

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
	public Icon getIcon(Activity<?> arg0) {
		return VOServicesPerspective.voIcon;
	}

}
