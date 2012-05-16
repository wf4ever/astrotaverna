package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivity;
import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.CoordTransformationConfigureAction;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;


@SuppressWarnings("serial")
public class CoordTransformationContextualView extends ContextualView {
	private final CoordTransformationActivity activity;
	private JTextArea description = new JTextArea("ads");
	//private JTextArea description = new JLabel("ads");

	public CoordTransformationContextualView(CoordTransformationActivity activity) {
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
		return "Stilts service: Coordenates transformation";
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
		
		description.setText("The service returns a table with a new column. "
						  + "\n Its value is calculated using the function" 
						  + "\n selected in configuration. Its parameters"
						  + "\n are the columns especified as inports");
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
		return new CoordTransformationConfigureAction(activity, owner);
	}

}
