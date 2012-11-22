package org.purl.wf4ever.astrotaverna.pdl.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;

@SuppressWarnings("serial")
public class PDLServiceConfigureAction
		extends
		ActivityConfigurationAction<PDLServiceActivity,
        PDLServiceActivityConfigurationBean> {

	public PDLServiceConfigureAction(PDLServiceActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<PDLServiceActivity, PDLServiceActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		PDLServiceConfigurationPanel panel = new PDLServiceConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<PDLServiceActivity,
        PDLServiceActivityConfigurationBean> dialog = new ActivityConfigurationDialog<PDLServiceActivity, PDLServiceActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
