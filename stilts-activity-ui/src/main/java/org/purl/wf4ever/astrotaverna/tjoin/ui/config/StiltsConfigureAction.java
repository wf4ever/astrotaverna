package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivityConfigurationBean;

@SuppressWarnings("serial")
public class StiltsConfigureAction
		extends
		ActivityConfigurationAction<TjoinActivity,
        TjoinActivityConfigurationBean> {

	public StiltsConfigureAction(TjoinActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<TjoinActivity, TjoinActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		StiltsConfigurationPanel panel = new StiltsConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<TjoinActivity,
        TjoinActivityConfigurationBean> dialog = new ActivityConfigurationDialog<TjoinActivity, TjoinActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
