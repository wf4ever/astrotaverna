package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.SelectRowsConfigureAction;

public class SelectRowsConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<SelectRowsActivity> {

	public SelectRowsConfigureMenuAction() {
		super(SelectRowsActivity.class);
	}

	@Override
	protected Action createAction() {
		SelectRowsActivity a = findActivity();
		Action result = null;
		result = new SelectRowsConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
