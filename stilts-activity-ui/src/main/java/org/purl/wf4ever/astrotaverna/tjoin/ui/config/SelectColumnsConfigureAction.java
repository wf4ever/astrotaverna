package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.SelectColumnsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectColumnsActivityConfigurationBean;

@SuppressWarnings("serial")
public class SelectColumnsConfigureAction
		extends
		ActivityConfigurationAction<SelectColumnsActivity,
        SelectColumnsActivityConfigurationBean> {

	public SelectColumnsConfigureAction(SelectColumnsActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<SelectColumnsActivity, SelectColumnsActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		SelectColumnsConfigurationPanel panel = new SelectColumnsConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<SelectColumnsActivity,
        SelectColumnsActivityConfigurationBean> dialog = new ActivityConfigurationDialog<SelectColumnsActivity, SelectColumnsActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
