package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tcat.TcatListActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.TcatListConfigureAction;

public class TcatListConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<TcatListActivity> {

	public TcatListConfigureMenuAction() {
		super(TcatListActivity.class);
	}

	@Override
	protected Action createAction() {
		TcatListActivity a = findActivity();
		Action result = null;
		result = new TcatListConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
