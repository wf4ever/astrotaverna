package uk.org.taverna.astro.astrotaverna.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import uk.org.taverna.astro.astrotaverna.AstroActivity;
import uk.org.taverna.astro.astrotaverna.AstroActivityConfigurationBean;
import uk.org.taverna.astro.astrotaverna.ui.config.AstroConfigureAction;

@SuppressWarnings("serial")
public class AstroContextualView extends ContextualView {
	private final AstroActivity activity;
	private JLabel description = new JLabel("ads");

	public AstroContextualView(AstroActivity activity) {
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
		AstroActivityConfigurationBean configuration = activity
				.getConfiguration();
		return "Astro service " + configuration.getExampleString();
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		AstroActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("Astro service " + configuration.getExampleUri()
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
		return new AstroConfigureAction(activity, owner);
	}

}
