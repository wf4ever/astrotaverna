package org.purl.wf4ever.astrotaverna.coordinates.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.purl.wf4ever.astrotaverna.coordinates.CoordinatesActivity;
import org.purl.wf4ever.astrotaverna.coordinates.CoordinatesActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.coordinates.ui.config.CoordinatesConfigureAction;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;


@SuppressWarnings("serial")
public class CoordinatesContextualView extends ContextualView {
	private final CoordinatesActivity activity;
	private JLabel description = new JLabel("ads");

	public CoordinatesContextualView(CoordinatesActivity activity) {
		this.activity = activity;
		initView();
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		jPanel.add(description);
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		CoordinatesActivityConfigurationBean configuration = activity
				.getConfiguration();
		return "coordinates service " + configuration.getExampleString();
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		CoordinatesActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("coordinates service " + configuration.getExampleUri()
				+ " - " + configuration.getExampleString());
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	}
	
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new CoordinatesConfigureAction(activity, owner);
	}

}
