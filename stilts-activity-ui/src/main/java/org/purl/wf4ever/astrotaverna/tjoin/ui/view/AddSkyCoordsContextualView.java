package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.AddSkyCoordsConfigureAction;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;


@SuppressWarnings("serial")
public class AddSkyCoordsContextualView extends ContextualView {
	private final AddSkyCoordsActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;

	public AddSkyCoordsContextualView(AddSkyCoordsActivity activity) {
		this.activity = activity;
		initView(); //this method will call the getMainFrame()
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		description = new JTextArea();
		
		jPanel.setLayout(new java.awt.BorderLayout());
		
		description.setEditable(false);
		description.setColumns(30);
		description.setLineWrap(true);
		description.setText("The service adds two columns to the table representing position on the sky. " +
				"The values are determined by converting a sky position whose coordinates are contained in existing columns. " +
				"The units input provides the existing units of the existing coordinates (degrees, radians or sexagesimal). " +
				"A column may be referenced by its name or its column index ($1, $2, $3, ...)" +
				"Using the configure service option you can choose between direct votable input, " +
				"a URL or a File. If the input is a file path then the output is a File path whereas the output " +
				"is a string with the votable in the remaining cases. The configure option also allow selection of the " +
				"input and output coordinate systems. ");
		
		jScrollPane1.setViewportView(description);
		jPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		//StiltsActivityConfigurationBean configuration = activity
		//		.getConfiguration();
		//return "Stilts service " + configuration.getExampleString();
		return "Coord. transformation";
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
		return new AddSkyCoordsConfigureAction(activity, owner);
	}

}
