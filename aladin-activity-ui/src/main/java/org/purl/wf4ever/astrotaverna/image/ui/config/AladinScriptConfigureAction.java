package org.purl.wf4ever.astrotaverna.image.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import org.purl.wf4ever.astrotaverna.aladin.AladinScriptActivity;
import org.purl.wf4ever.astrotaverna.aladin.AladinScriptActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;



@SuppressWarnings("serial")
public class AladinScriptConfigureAction
		extends
		ActivityConfigurationAction<AladinScriptActivity,
        AladinScriptActivityConfigurationBean> {

	public AladinScriptConfigureAction(AladinScriptActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AladinScriptActivity, AladinScriptActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AladinScriptConfigurationPanel panel = new AladinScriptConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AladinScriptActivity,
        AladinScriptActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AladinScriptActivity, AladinScriptActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
