package org.purl.wf4ever.astrotaverna.image.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivity;
import org.purl.wf4ever.astrotaverna.image.ui.config.AladinMacroConfigureAction;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;



@SuppressWarnings("serial")
public class AladinMacroContextualView extends ContextualView {
	private final AladinMacroActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;

	public AladinMacroContextualView(AladinMacroActivity activity) {
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
		description.setText("It executes an Aladin macro by receiving a macro and a parameters file. You may choose between direct input or file input and also you may see the Aladin window if you choose gui option");
		
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
		return "Aladin macro";
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
		return new AladinMacroConfigureAction(activity, owner);
	}

}
