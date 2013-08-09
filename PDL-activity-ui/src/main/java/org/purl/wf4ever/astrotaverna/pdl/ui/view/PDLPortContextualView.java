package org.purl.wf4ever.astrotaverna.pdl.ui.view;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JComponent;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;

import net.ivoa.parameter.model.SingleParameter;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;


//import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;



@SuppressWarnings("serial")
public class PDLPortContextualView extends ContextualView {
	//private final PDLServiceActivity activity;
	//private final ActivityInputPort inputPort;
	private String paramDesc;
	//private final String defaultValue;
	private final String paramName;
	
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;
	
	

	public PDLPortContextualView(ActivityInputPort inputPort, PDLServiceActivity activity) {
		//this.inputPort = inputPort;	
		//this.activity = activity;
		
		SingleParameter param = activity.getHashAllParameters().get(inputPort.getName());
		
		
		paramDesc ="";
		paramName = inputPort.getName();
		if(param!=null){
			if(param.getDependency()!=null)
				paramDesc+="The parameter is "+ param.getDependency()+".\n";
			if(param.getParameterType()!=null)
				paramDesc += "Type: "+ param.getParameterType().toString()+".\n";
			//if(param.getPrecision()!=null)
			//	paramDesc += "Precision: " + param.getPrecision().toString()+".\n";
			if(param.getUCD()!=null)
				paramDesc += "UCD: "+ param.getUCD()+"\n";
			if(param.getUType()!=null)
				paramDesc += "UType: "+ param.getUType()+"\n";
			if(param.getSkossConcept()!=null)
				paramDesc += "SKOS: " + param.getSkossConcept()+"\n";
			if(param.getUnit()!=null)
				paramDesc += "Unit: " + param.getUnit();

		}
		
		initView(); //this method will call the getMainFrame()
	}
	
	public PDLPortContextualView(ActivityOutputPort outputPort, PDLServiceActivity activity) {
		//this.inputPort = inputPort;	
		//this.activity = activity;
		
		HashMap map = activity.getHashAllParameters();
		//this is needed in case some output port has spaces
		HashMap transformedMap = new HashMap<String, String>();
		
		for(Entry<String, SingleParameter> entry : (Set<Entry>)map.entrySet()){
			transformedMap.put(entry.getValue().getName().replaceAll(" ", "_"), entry.getKey());
		}
		
		SingleParameter param = activity.getHashAllParameters().get(transformedMap.get(outputPort.getName()));
		
		
	
		
		
		
		paramDesc ="";
		paramName = outputPort.getName();
		if(param!=null){
			if(param.getDependency()!=null)
				paramDesc+="The parameter is "+ param.getDependency()+".\n";
			if(param.getParameterType()!=null)
				paramDesc += "Type: "+ param.getParameterType().toString()+".\n";
			//if(param.getPrecision()!=null)
			//	paramDesc += "Precision: " + param.getPrecision().toString()+".\n";
			if(param.getUCD()!=null)
				paramDesc += "UCD: "+ param.getUCD()+"\n";
			if(param.getUType()!=null)
				paramDesc += "UType: "+ param.getUType()+"\n";
			if(param.getSkossConcept()!=null)
				paramDesc += "SKOS: " + param.getSkossConcept()+"\n";
			if(param.getUnit()!=null)
				paramDesc += "Unit: " + param.getUnit();
		}
		
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
		description.setText(paramDesc);

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
		return "Port: "+ paramName;
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
		
		//IMPLEMENT THIS METHOD: IF THE CONFIGURATIO CHANGES, IT MIGHT BE A DIFFERENT PDL DESCRIPTION
		//-----------------------------------------
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
		//return new PDLServiceConfigureAction(activity, owner);
		return null;
	}

}
