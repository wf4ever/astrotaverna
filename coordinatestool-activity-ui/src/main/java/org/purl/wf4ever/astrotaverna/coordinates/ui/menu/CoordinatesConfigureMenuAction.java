package org.purl.wf4ever.astrotaverna.coordinates.ui.menu;

import javax.swing.Action;

import org.purl.wf4ever.astrotaverna.coordinates.CoordinatesActivity;
import org.purl.wf4ever.astrotaverna.coordinates.ui.config.CoordinatesConfigureAction;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

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
