package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.AddSkyCoordsConfigureAction;

public class AddSkyCoordsConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AddSkyCoordsActivity> {

	public AddSkyCoordsConfigureMenuAction() {
		super(AddSkyCoordsActivity.class);
	}

	@Override
	protected Action createAction() {
		AddSkyCoordsActivity a = findActivity();
		Action result = null;
		result = new AddSkyCoordsConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
