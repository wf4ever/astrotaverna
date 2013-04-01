package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import org.purl.wf4ever.astrotaverna.tjoin.CrossMatch2Activity;
import org.purl.wf4ever.astrotaverna.tjoin.CrossMatch2ActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;


@SuppressWarnings("serial")
public class CrossMatch2ConfigureAction
		extends
		ActivityConfigurationAction<CrossMatch2Activity,
        CrossMatch2ActivityConfigurationBean> {

	public CrossMatch2ConfigureAction(CrossMatch2Activity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<CrossMatch2Activity, CrossMatch2ActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		CrossMatch2ConfigurationPanel panel = new CrossMatch2ConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<CrossMatch2Activity,
        CrossMatch2ActivityConfigurationBean> dialog = new ActivityConfigurationDialog<CrossMatch2Activity, CrossMatch2ActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
