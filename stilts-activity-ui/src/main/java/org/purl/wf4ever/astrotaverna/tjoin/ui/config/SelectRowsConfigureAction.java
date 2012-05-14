package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivityConfigurationBean;

@SuppressWarnings("serial")
public class SelectRowsConfigureAction
		extends
		ActivityConfigurationAction<SelectRowsActivity,
        SelectRowsActivityConfigurationBean> {

	public SelectRowsConfigureAction(SelectRowsActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<SelectRowsActivity, SelectRowsActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		SelectRowsConfigurationPanel panel = new SelectRowsConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<SelectRowsActivity,
        SelectRowsActivityConfigurationBean> dialog = new ActivityConfigurationDialog<SelectRowsActivity, SelectRowsActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
