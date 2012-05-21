package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.ResolveCoordsActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.ResolveCoordsConfigureAction;

public class ResolveCoordsConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<ResolveCoordsActivity> {

	public ResolveCoordsConfigureMenuAction() {
		super(ResolveCoordsActivity.class);
	}

	@Override
	protected Action createAction() {
		ResolveCoordsActivity a = findActivity();
		Action result = null;
		result = new ResolveCoordsConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
