package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import org.purl.wf4ever.astrotaverna.tjoin.ui.config.AddCommonRowToVOTableConfigureAction;
import org.purl.wf4ever.astrotaverna.voutils.AddCommonRowToVOTableActivity;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;


public class AddCommonRowToVOTableConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AddCommonRowToVOTableActivity> {

	public AddCommonRowToVOTableConfigureMenuAction() {
		super(AddCommonRowToVOTableActivity.class);
	}

	@Override
	protected Action createAction() {
		AddCommonRowToVOTableActivity a = findActivity();
		Action result = null;
		result = new AddCommonRowToVOTableConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
