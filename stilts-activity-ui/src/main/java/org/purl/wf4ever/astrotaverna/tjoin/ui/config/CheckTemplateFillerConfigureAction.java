package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.voutils.CheckTemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.voutils.CheckTemplateFillerActivityConfigurationBean;

@SuppressWarnings("serial")
public class CheckTemplateFillerConfigureAction
		extends
		ActivityConfigurationAction<CheckTemplateFillerActivity,
        CheckTemplateFillerActivityConfigurationBean> {

	public CheckTemplateFillerConfigureAction(CheckTemplateFillerActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<CheckTemplateFillerActivity, CheckTemplateFillerActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		CheckTemplateFillerConfigurationPanel panel = new CheckTemplateFillerConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<CheckTemplateFillerActivity,
        CheckTemplateFillerActivityConfigurationBean> dialog = new ActivityConfigurationDialog<CheckTemplateFillerActivity, CheckTemplateFillerActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
