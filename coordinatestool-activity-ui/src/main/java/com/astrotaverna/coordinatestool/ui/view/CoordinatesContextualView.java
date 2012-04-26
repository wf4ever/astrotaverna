package com.astrotaverna.coordinatestool.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import com.astrotaverna.coordinatestool.CoordinatesActivity;
import com.astrotaverna.coordinatestool.CoordinatesActivityConfigurationBean;
import com.astrotaverna.coordinatestool.ui.config.CoordinatesConfigureAction;

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
