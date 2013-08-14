package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.tcat.TcatListActivity;
import org.purl.wf4ever.astrotaverna.tcat.TcatActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.TcatListConfigureAction;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;


@SuppressWarnings("serial")
public class TcatListContextualView extends ContextualView {
	private final TcatListActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;

	public TcatListContextualView(TcatListActivity activity) {
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
		description.setText("The service returns a votable resulting from concatenating a list of similar votables. " +
				"The tables must be of similar form to each other (same number and types of columns). \n" +
				"Using the configure service option you can choose between direct list of votable input, " +
				"URLs or Files. If the input are files path then the output is a File path whereas the output " +
				"is a string with the votable in the remaining cases. ");

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
		return "Cat VOTable list";
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
		return new TcatListConfigureAction(activity, owner);
	}

}
