package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivity;
import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivityConfigurationBean;

@SuppressWarnings("serial")
public class GetListFromColumnConfigureAction
		extends
		ActivityConfigurationAction<GetListFromColumnActivity,
        GetListFromColumnActivityConfigurationBean> {

	public GetListFromColumnConfigureAction(GetListFromColumnActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<GetListFromColumnActivity, GetListFromColumnActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		GetListFromColumnConfigurationPanel panel = new GetListFromColumnConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<GetListFromColumnActivity,
        GetListFromColumnActivityConfigurationBean> dialog = new ActivityConfigurationDialog<GetListFromColumnActivity, GetListFromColumnActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
