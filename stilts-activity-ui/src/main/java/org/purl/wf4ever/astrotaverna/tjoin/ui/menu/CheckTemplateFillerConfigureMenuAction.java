package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.voutils.CheckTemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.CheckTemplateFillerConfigureAction;

public class CheckTemplateFillerConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<CheckTemplateFillerActivity> {

	public CheckTemplateFillerConfigureMenuAction() {
		super(CheckTemplateFillerActivity.class);
	}

	@Override
	protected Action createAction() {
		CheckTemplateFillerActivity a = findActivity();
		Action result = null;
		result = new CheckTemplateFillerConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
