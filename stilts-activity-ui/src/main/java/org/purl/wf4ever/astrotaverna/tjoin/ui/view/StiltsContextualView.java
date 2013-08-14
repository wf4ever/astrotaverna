package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;

@SuppressWarnings("serial")
public class StiltsContextualView extends ContextualView {
	private final TjoinActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;
	//private JTextArea description = new JLabel("ads");

	public StiltsContextualView(TjoinActivity activity) {
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
		description.setText("The service makes the join of two votables tables with the same " +
				"number of rows. Using the configure service option you can choose between direct vo table input, " +
				"a URL or a File. If the input is a file path then the output is a File path whereas the output " +
				"is a string with the votable in the remaining cases.");
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
		return "Join VOTables";
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
		
		//description.setText("The service makes the join of two tables with the same format and the same " +
		//		"\n number of rows. The inputs are the names of the files or URLsthat contain the tables " +
		//		"\n and the name of the output file. All the tables must have the same number of " +
		//		"\n rows");
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
		return new StiltsConfigureAction(activity, owner);
	}

}
