package org.purl.wf4ever.astrotaverna.pdl.ui.view;

import java.awt.Frame;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JComponent;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
//import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.pdl.ui.config.PDLServiceConfigureAction;



@SuppressWarnings("serial")
public class PDLServiceContextualView extends ContextualView {
	private final PDLServiceActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;

	public PDLServiceContextualView(PDLServiceActivity activity) {
		this.activity = activity;
		initView(); //this method will call the getMainFrame()
	}

	@Override
	public JComponent getMainFrame() {
		String serviceDescription;
		JPanel jPanel = new JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		description = new JTextArea();
		
		jPanel.setLayout(new java.awt.BorderLayout());
		
		serviceDescription = activity.getServiceDescription();
		if(serviceDescription==null)
			serviceDescription="";
		else
			serviceDescription+="\n";
		
		HashMap<String,String> restrictions = activity.getRestrictionsOnGroups();
		if(restrictions!=null)
			for(String key: restrictions.keySet()){
				serviceDescription+="Restrictions on parameter group (" + key +"):\n";
				serviceDescription+=restrictions.get(key);
			}
		
		description.setEditable(false);
		description.setColumns(30);
		description.setLineWrap(true);
		description.setText(serviceDescription);
		//description.setText("Import PDL service by providing a pdl-description file using the " +
		//		"configure service option. The tool will have as much inputs and outputs as described in the" +
		//		"pdl file. If the service is asynchronous it, it waits until the service is finished.");
		
		
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
		return "PDL service";
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
		return new PDLServiceConfigureAction(activity, owner);
	}

}
