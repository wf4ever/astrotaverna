package org.purl.wf4ever.astrotaverna.image.ui.menu;

import javax.swing.Action;

import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivity;
import org.purl.wf4ever.astrotaverna.image.ui.config.AladinMacroConfigureAction;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;


public class AladinMacroConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AladinMacroActivity> {

	public AladinMacroConfigureMenuAction() {
		super(AladinMacroActivity.class);
	}

	@Override
	protected Action createAction() {
		AladinMacroActivity a = findActivity();
		Action result = null;
		result = new AladinMacroConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
