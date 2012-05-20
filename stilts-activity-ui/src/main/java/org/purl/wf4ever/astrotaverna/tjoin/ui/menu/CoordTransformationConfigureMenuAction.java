package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.CoordTransformationConfigureAction;

public class CoordTransformationConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<CoordTransformationActivity> {

	public CoordTransformationConfigureMenuAction() {
		super(CoordTransformationActivity.class);
	}

	@Override
	protected Action createAction() {
		CoordTransformationActivity a = findActivity();
		Action result = null;
		result = new CoordTransformationConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
