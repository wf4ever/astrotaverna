package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivityConfigurationBean;

@SuppressWarnings("serial")
public class AddSkyCoordsConfigureAction
		extends
		ActivityConfigurationAction<AddSkyCoordsActivity,
        AddSkyCoordsActivityConfigurationBean> {

	public AddSkyCoordsConfigureAction(AddSkyCoordsActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AddSkyCoordsActivity, AddSkyCoordsActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AddSkyCoordsConfigurationPanel panel = new AddSkyCoordsConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AddSkyCoordsActivity,
        AddSkyCoordsActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AddSkyCoordsActivity, AddSkyCoordsActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
