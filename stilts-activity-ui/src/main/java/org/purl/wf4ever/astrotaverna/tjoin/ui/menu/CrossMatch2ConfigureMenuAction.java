package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

import org.purl.wf4ever.astrotaverna.tjoin.CrossMatch2Activity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.CrossMatch2ConfigureAction;

public class CrossMatch2ConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<CrossMatch2Activity> {

	public CrossMatch2ConfigureMenuAction() {
		super(CrossMatch2Activity.class);
	}

	@Override
	protected Action createAction() {
		CrossMatch2Activity a = findActivity();
		Action result = null;
		result = new CrossMatch2ConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
