package org.purl.wf4ever.astrotaverna.pdl.ui.menu;

import javax.swing.Action;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;
import org.purl.wf4ever.astrotaverna.pdl.ui.config.ValidationPDLClientConfigureAction;

public class ValidationPDLClientConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<ValidationPDLClientActivity> {

	public ValidationPDLClientConfigureMenuAction() {
		super(ValidationPDLClientActivity.class);
	}

	@Override
	protected Action createAction() {
		ValidationPDLClientActivity a = findActivity();
		Action result = null;
		result = new ValidationPDLClientConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
	}

}
