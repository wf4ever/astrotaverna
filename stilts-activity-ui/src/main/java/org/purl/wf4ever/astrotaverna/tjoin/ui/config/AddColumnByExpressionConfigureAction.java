package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.AddColumnByExpressionActivity;
import org.purl.wf4ever.astrotaverna.tpipe.AddColumnByExpressionActivityConfigurationBean;

@SuppressWarnings("serial")
public class AddColumnByExpressionConfigureAction
		extends
		ActivityConfigurationAction<AddColumnByExpressionActivity,
        AddColumnByExpressionActivityConfigurationBean> {

	public AddColumnByExpressionConfigureAction(AddColumnByExpressionActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AddColumnByExpressionActivity, AddColumnByExpressionActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AddColumnByExpressionConfigurationPanel panel = new AddColumnByExpressionConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AddColumnByExpressionActivity,
        AddColumnByExpressionActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AddColumnByExpressionActivity, AddColumnByExpressionActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
