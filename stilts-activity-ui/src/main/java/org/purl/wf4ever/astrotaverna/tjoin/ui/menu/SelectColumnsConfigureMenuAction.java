package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.SelectColumnsActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.SelectColumnsConfigureAction;

public class SelectColumnsConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<SelectColumnsActivity> {

	public SelectColumnsConfigureMenuAction() {
		super(SelectColumnsActivity.class);
	}

	@Override
	protected Action createAction() {
		SelectColumnsActivity a = findActivity();
		Action result = null;
		result = new SelectColumnsConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
