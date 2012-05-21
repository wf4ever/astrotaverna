package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.ResolveCoordsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.ResolveCoordsActivityConfigurationBean;

@SuppressWarnings("serial")
public class ResolveCoordsConfigureAction
		extends
		ActivityConfigurationAction<ResolveCoordsActivity,
        ResolveCoordsActivityConfigurationBean> {

	public ResolveCoordsConfigureAction(ResolveCoordsActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<ResolveCoordsActivity, ResolveCoordsActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		ResolveCoordsConfigurationPanel panel = new ResolveCoordsConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<ResolveCoordsActivity,
        ResolveCoordsActivityConfigurationBean> dialog = new ActivityConfigurationDialog<ResolveCoordsActivity, ResolveCoordsActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
