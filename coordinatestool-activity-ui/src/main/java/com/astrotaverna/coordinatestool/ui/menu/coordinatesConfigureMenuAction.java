package com.astrotaverna.coordinatestool.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import com.astrotaverna.coordinatestool.coordinatesActivity;
import com.astrotaverna.coordinatestool.ui.config.coordinatesConfigureAction;

public class coordinatesConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<coordinatesActivity> {

	public coordinatesConfigureMenuAction() {
		super(coordinatesActivity.class);
	}

	@Override
	protected Action createAction() {
		coordinatesActivity a = findActivity();
		Action result = null;
		result = new coordinatesConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
