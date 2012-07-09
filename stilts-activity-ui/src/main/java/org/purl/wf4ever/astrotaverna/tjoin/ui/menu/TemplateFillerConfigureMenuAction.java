package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.TemplateFillerConfigureAction;

public class TemplateFillerConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<TemplateFillerActivity> {

	public TemplateFillerConfigureMenuAction() {
		super(TemplateFillerActivity.class);
	}

	@Override
	protected Action createAction() {
		TemplateFillerActivity a = findActivity();
		Action result = null;
		result = new TemplateFillerConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
