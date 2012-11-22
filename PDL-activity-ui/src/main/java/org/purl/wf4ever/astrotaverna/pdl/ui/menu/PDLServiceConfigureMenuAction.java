package org.purl.wf4ever.astrotaverna.pdl.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.ui.config.PDLServiceConfigureAction;

public class PDLServiceConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<PDLServiceActivity> {

	public PDLServiceConfigureMenuAction() {
		super(PDLServiceActivity.class);
	}

	@Override
	protected Action createAction() {
		PDLServiceActivity a = findActivity();
		Action result = null;
		result = new PDLServiceConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
