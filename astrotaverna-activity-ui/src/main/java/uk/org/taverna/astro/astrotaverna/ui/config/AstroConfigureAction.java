package uk.org.taverna.astro.astrotaverna.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import uk.org.taverna.astro.astrotaverna.AstroActivity;
import uk.org.taverna.astro.astrotaverna.AstroActivityConfigurationBean;

@SuppressWarnings("serial")
public class AstroConfigureAction
		extends
		ActivityConfigurationAction<AstroActivity,
        AstroActivityConfigurationBean> {

	public AstroConfigureAction(AstroActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<AstroActivity, AstroActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		AstroConfigurationPanel panel = new AstroConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<AstroActivity,
        AstroActivityConfigurationBean> dialog = new ActivityConfigurationDialog<AstroActivity, AstroActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
