package com.astrotaverna.coordinatestool.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import com.astrotaverna.coordinatestool.CoordinatesActivity;
import com.astrotaverna.coordinatestool.ui.config.CoordinatesConfigureAction;

public class CoordinatesConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<CoordinatesActivity> {

	public CoordinatesConfigureMenuAction() {
		super(CoordinatesActivity.class);
	}

	@Override
	protected Action createAction() {
		CoordinatesActivity a = findActivity();
		Action result = null;
		result = new CoordinatesConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
