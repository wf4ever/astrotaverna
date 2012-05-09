package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;

public class StiltsConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<TjoinActivity> {

	public StiltsConfigureMenuAction() {
		super(TjoinActivity.class);
	}

	@Override
	protected Action createAction() {
		TjoinActivity a = findActivity();
		Action result = null;
		result = new StiltsConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
