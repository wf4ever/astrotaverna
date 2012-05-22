package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tcat.TcatActivity;
import org.purl.wf4ever.astrotaverna.tcat.TcatActivityConfigurationBean;

@SuppressWarnings("serial")
public class TcatConfigureAction
		extends
		ActivityConfigurationAction<TcatActivity,
        TcatActivityConfigurationBean> {

	public TcatConfigureAction(TcatActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<TcatActivity, TcatActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		TcatConfigurationPanel panel = new TcatConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<TcatActivity,
        TcatActivityConfigurationBean> dialog = new ActivityConfigurationDialog<TcatActivity, TcatActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
