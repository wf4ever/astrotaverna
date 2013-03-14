package org.purl.wf4ever.astrotaverna.image.ui.menu;

import javax.swing.Action;

import org.purl.wf4ever.astrotaverna.aladin.AladinScriptActivity;
import org.purl.wf4ever.astrotaverna.image.ui.config.AladinScriptConfigureAction;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;


public class AladinScriptConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AladinScriptActivity> {

	public AladinScriptConfigureMenuAction() {
		super(AladinScriptActivity.class);
	}

	@Override
	protected Action createAction() {
		AladinScriptActivity a = findActivity();
		Action result = null;
		result = new AladinScriptConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
