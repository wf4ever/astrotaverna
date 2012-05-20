package org.purl.wf4ever.astrotaverna.tjoin.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.tpipe.FormatConversionActivity;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.FormatConversionConfigureAction;

public class FormatConversionConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<FormatConversionActivity> {

	public FormatConversionConfigureMenuAction() {
		super(FormatConversionActivity.class);
	}

	@Override
	protected Action createAction() {
		FormatConversionActivity a = findActivity();
		Action result = null;
		result = new FormatConversionConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
