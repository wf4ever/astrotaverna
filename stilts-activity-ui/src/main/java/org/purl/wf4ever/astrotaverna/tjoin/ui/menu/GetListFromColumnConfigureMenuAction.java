package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.GetListFromColumnConfigureAction;

public class GetListFromColumnConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<GetListFromColumnActivity> {

	public GetListFromColumnConfigureMenuAction() {
		super(GetListFromColumnActivity.class);
	}

	@Override
	protected Action createAction() {
		GetListFromColumnActivity a = findActivity();
		Action result = null;
		result = new GetListFromColumnConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
