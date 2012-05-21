package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.AddColumnByExpressionActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.AddColumnByExpressionConfigureAction;

public class AddColumnByExpressionConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AddColumnByExpressionActivity> {

	public AddColumnByExpressionConfigureMenuAction() {
		super(AddColumnByExpressionActivity.class);
	}

	@Override
	protected Action createAction() {
		AddColumnByExpressionActivity a = findActivity();
		Action result = null;
		result = new AddColumnByExpressionConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
