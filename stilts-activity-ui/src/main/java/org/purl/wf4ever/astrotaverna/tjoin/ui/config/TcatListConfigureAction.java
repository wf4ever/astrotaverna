package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tcat.TcatListActivity;
import org.purl.wf4ever.astrotaverna.tcat.TcatListActivityConfigurationBean;

@SuppressWarnings("serial")
public class TcatListConfigureAction
		extends
		ActivityConfigurationAction<TcatListActivity,
        TcatListActivityConfigurationBean> {

	public TcatListConfigureAction(TcatListActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<TcatListActivity, TcatListActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		TcatListConfigurationPanel panel = new TcatListConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<TcatListActivity,
        TcatListActivityConfigurationBean> dialog = new ActivityConfigurationDialog<TcatListActivity, TcatListActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
