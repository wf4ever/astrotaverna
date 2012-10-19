package org.purl.wf4ever.astrotaverna.pdl.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;
import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivityConfigurationBean;

@SuppressWarnings("serial")
public class ValidationPDLClientConfigureAction
		extends
		ActivityConfigurationAction<ValidationPDLClientActivity,
        ValidationPDLClientActivityConfigurationBean> {

	public ValidationPDLClientConfigureAction(ValidationPDLClientActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<ValidationPDLClientActivity, ValidationPDLClientActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		ValidationPDLClientConfigurationPanel panel = new ValidationPDLClientConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<ValidationPDLClientActivity,
        ValidationPDLClientActivityConfigurationBean> dialog = new ActivityConfigurationDialog<ValidationPDLClientActivity, ValidationPDLClientActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
