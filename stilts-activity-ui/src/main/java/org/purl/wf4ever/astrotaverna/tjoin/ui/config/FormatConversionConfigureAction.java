package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.FormatConversionActivity;
import org.purl.wf4ever.astrotaverna.tpipe.FormatConversionActivityConfigurationBean;

@SuppressWarnings("serial")
public class FormatConversionConfigureAction
		extends
		ActivityConfigurationAction<FormatConversionActivity,
        FormatConversionActivityConfigurationBean> {

	public FormatConversionConfigureAction(FormatConversionActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<FormatConversionActivity, FormatConversionActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		FormatConversionConfigurationPanel panel = new FormatConversionConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<FormatConversionActivity,
        FormatConversionActivityConfigurationBean> dialog = new ActivityConfigurationDialog<FormatConversionActivity, FormatConversionActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
