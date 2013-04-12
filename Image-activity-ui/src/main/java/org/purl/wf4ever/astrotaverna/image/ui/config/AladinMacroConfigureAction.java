package org.purl.wf4ever.astrotaverna.image.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivity;
import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;



@SuppressWarnings("serial")
public class AladinMacroConfigureAction
		extends
		ActivityConfigurationAction<AladinMacroActivity,
        AladinMacroActivityConfigurationBean> {

	public AladinMacroConfigureAction(AladinMacroActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AladinMacroActivity, AladinMacroActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AladinMacroConfigurationPanel panel = new AladinMacroConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AladinMacroActivity,
        AladinMacroActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AladinMacroActivity, AladinMacroActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
