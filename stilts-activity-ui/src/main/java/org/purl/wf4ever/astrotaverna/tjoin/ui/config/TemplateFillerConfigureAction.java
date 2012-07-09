package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivityConfigurationBean;

@SuppressWarnings("serial")
public class TemplateFillerConfigureAction
		extends
		ActivityConfigurationAction<TemplateFillerActivity,
        TemplateFillerActivityConfigurationBean> {

	public TemplateFillerConfigureAction(TemplateFillerActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<TemplateFillerActivity, TemplateFillerActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		TemplateFillerConfigurationPanel panel = new TemplateFillerConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<TemplateFillerActivity,
        TemplateFillerActivityConfigurationBean> dialog = new ActivityConfigurationDialog<TemplateFillerActivity, TemplateFillerActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
