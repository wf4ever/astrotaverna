package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tcat.TcatActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.TcatConfigureAction;

public class TcatConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<TcatActivity> {

	public TcatConfigureMenuAction() {
		super(TcatActivity.class);
	}

	@Override
	protected Action createAction() {
		TcatActivity a = findActivity();
		Action result = null;
		result = new TcatConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
