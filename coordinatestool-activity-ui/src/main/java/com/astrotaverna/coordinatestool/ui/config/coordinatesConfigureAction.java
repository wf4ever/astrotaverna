package com.astrotaverna.coordinatestool.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import com.astrotaverna.coordinatestool.coordinatesActivity;
import com.astrotaverna.coordinatestool.coordinatesActivityConfigurationBean;

@SuppressWarnings("serial")
public class coordinatesConfigureAction
		extends
		ActivityConfigurationAction<coordinatesActivity,
        coordinatesActivityConfigurationBean> {

	public coordinatesConfigureAction(coordinatesActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<coordinatesActivity, coordinatesActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		coordinatesConfigurationPanel panel = new coordinatesConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<coordinatesActivity,
        coordinatesActivityConfigurationBean> dialog = new ActivityConfigurationDialog<coordinatesActivity, coordinatesActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
