package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import org.purl.wf4ever.astrotaverna.voutils.AddCommonRowToVOTableActivity;
import org.purl.wf4ever.astrotaverna.voutils.AddCommonRowToVOTableActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;



@SuppressWarnings("serial")
public class AddCommonRowToVOTableConfigureAction
		extends
		ActivityConfigurationAction<AddCommonRowToVOTableActivity,
        AddCommonRowToVOTableActivityConfigurationBean> {

	public AddCommonRowToVOTableConfigureAction(AddCommonRowToVOTableActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AddCommonRowToVOTableActivity, AddCommonRowToVOTableActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AddCommonRowToVOTableConfigurationPanel panel = new AddCommonRowToVOTableConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AddCommonRowToVOTableActivity,
        AddCommonRowToVOTableActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AddCommonRowToVOTableActivity, AddCommonRowToVOTableActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
