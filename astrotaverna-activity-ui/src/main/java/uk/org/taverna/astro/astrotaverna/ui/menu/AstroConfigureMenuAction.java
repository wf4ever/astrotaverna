package uk.org.taverna.astro.astrotaverna.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import uk.org.taverna.astro.astrotaverna.AstroActivity;
import uk.org.taverna.astro.astrotaverna.ui.config.AstroConfigureAction;

public class AstroConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<AstroActivity> {

	public AstroConfigureMenuAction() {
		super(AstroActivity.class);
	}

	@Override
	protected Action createAction() {
		AstroActivity a = findActivity();
		Action result = null;
		result = new AstroConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure example service");
		addMenuDots(result);
		return result;
	}

}
