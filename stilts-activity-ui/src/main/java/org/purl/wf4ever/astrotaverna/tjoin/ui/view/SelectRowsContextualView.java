package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.SelectRowsConfigureAction;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;


@SuppressWarnings("serial")
public class SelectRowsContextualView extends ContextualView {
	private final SelectRowsActivity activity;
	private JTextArea description = new JTextArea("ads");
	//private JTextArea description = new JLabel("ads");

	public SelectRowsContextualView(SelectRowsActivity activity) {
		this.activity = activity;
		initView(); //this method will call the getMainFrame()
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		description.setEditable(false);
		description.setWrapStyleWord(false);
		jPanel.add(description);
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		//StiltsActivityConfigurationBean configuration = activity
		//		.getConfiguration();
		//return "Stilts service " + configuration.getExampleString();
		return "Stilts service: Rows selection";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		//StiltsActivityConfigurationBean configuration = activity
		//		.getConfiguration();		
		//description.setText("Stilts service " + configuration.getExampleUri()
		//		+ " - " + configuration.getExampleString());
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
		
		description.setText("The service returns a table aplying the filter (subset of rows). " 
						+ "\n The rows that makes true the expression filter are selected."
						+ "\n e.g. RA > 230 && DEC < 20 ");
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	} 
	
	//the view can return a configuration Action if the selection can be configured 
	//or customized. If this is not null, Taverna will add a Configure button to 
	//the section.
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new SelectRowsConfigureAction(activity, owner);
	}

}
