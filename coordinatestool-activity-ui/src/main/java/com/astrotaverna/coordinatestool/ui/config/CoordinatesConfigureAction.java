package com.astrotaverna.coordinatestool.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import com.astrotaverna.coordinatestool.CoordinatesActivity;
import com.astrotaverna.coordinatestool.CoordinatesActivityConfigurationBean;

@SuppressWarnings("serial")
public class CoordinatesConfigureAction
		extends
		ActivityConfigurationAction<CoordinatesActivity,
        CoordinatesActivityConfigurationBean> {

	public CoordinatesConfigureAction(CoordinatesActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<CoordinatesActivity, CoordinatesActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		CoordinatesConfigurationPanel panel = new CoordinatesConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<CoordinatesActivity,
        CoordinatesActivityConfigurationBean> dialog = new ActivityConfigurationDialog<CoordinatesActivity, CoordinatesActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
