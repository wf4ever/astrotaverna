package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivity;
import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivityConfigurationBean;

@SuppressWarnings("serial")
public class CoordTransformationConfigureAction
		extends
		ActivityConfigurationAction<CoordTransformationActivity,
        CoordTransformationActivityConfigurationBean> {

	public CoordTransformationConfigureAction(CoordTransformationActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<CoordTransformationActivity, CoordTransformationActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		CoordTransformationConfigurationPanel panel = new CoordTransformationConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<CoordTransformationActivity,
        CoordTransformationActivityConfigurationBean> dialog = new ActivityConfigurationDialog<CoordTransformationActivity, CoordTransformationActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
