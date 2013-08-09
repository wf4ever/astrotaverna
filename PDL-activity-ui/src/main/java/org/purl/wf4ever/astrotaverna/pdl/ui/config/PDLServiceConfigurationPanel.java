package org.purl.wf4ever.astrotaverna.pdl.ui.config;

import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import net.ivoa.parameter.model.Service;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

//import org.apache.log4j.Logger;
import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;


@SuppressWarnings("serial")
public class PDLServiceConfigurationPanel
		extends
		ActivityConfigurationPanel<PDLServiceActivity, 
        PDLServiceActivityConfigurationBean> {

	
	private PDLServiceActivity activity;
	private PDLServiceActivityConfigurationBean configBean;
	private PDLServiceActivityConfigurationBean aux;
	
	//String[] inputTypesStrings = {"File", "URL", "String"};
	//String[] filterTypesStrings = {"Column names", "UCDs"};
	
	private JTextField  urlField;
	private JComboBox typeOfService;
	


	public PDLServiceConfigurationPanel(PDLServiceActivity activity) {
		this.activity = activity;
		aux = new PDLServiceActivityConfigurationBean();
		initGui();
	}

	protected void initGui() {
		
		String[] serviceTypesStrings = {aux.PDLSERVICE, aux.RESTSERVICE, aux.VOTABLERESTSERVICE};
		
		removeAll();
		setLayout(new GridLayout(0, 2));

		// FIXME: Create GUI depending on activity configuration bean
		JLabel labelString = new JLabel("PDL-description URL:");
		add(labelString);
		urlField = new JTextField("");//http://www.exampleuri.com/pdldescriptionfile.xml
		add(urlField);
		labelString.setLabelFor(urlField);
			
		labelString = new JLabel("Type of service:");
		add(labelString);
		typeOfService = new JComboBox(serviceTypesStrings);
		add(typeOfService);
		labelString.setLabelFor(typeOfService);
		typeOfService.setSelectedIndex(1);
		
		// Populate fields from activity configuration bean
		refreshConfiguration();
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
		
		String errorMessage=null;
		
		String  urlInput = urlField.getText();
		
		try {
			URI uri = new URI(urlInput);
		} catch (Exception e) {
			errorMessage = "Invalid URI for the PDL description file";
		}
		
		String  type = (String)typeOfService.getSelectedItem();
		if(!(      type.compareTo(aux.PDLSERVICE)==0
				|| type.compareTo(aux.RESTSERVICE)==0
				|| type.compareTo(aux.VOTABLERESTSERVICE)==0)){
			errorMessage = "Valid type of services: " + aux.PDLSERVICE + " and " + aux.RESTSERVICE + " and " + aux.VOTABLERESTSERVICE;
		}
		
		if (errorMessage!=null){
			JOptionPane.showMessageDialog(this, errorMessage,
					"Invalid URI", JOptionPane.ERROR_MESSAGE);
			// Not valid, return false
			return false;
		}
		// All valid, return true
		return true;
	}

	/**
	 * Return configuration bean generated from user interface last time
	 * noteConfiguration() was called.
	 */
	@Override
	public PDLServiceActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		String originalURL = configBean.getPdlDescriptionFile();
		String originalTypeOfService = configBean.getServiceType();
		// true (changed) unless all fields match the originals
		
		return ! (originalURL.compareTo(urlField.getText())==0
				&& originalTypeOfService.compareTo((String)typeOfService.getSelectedItem())==0 ); 
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new PDLServiceActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setPdlDescriptionFile(urlField.getText());
		configBean.setServiceType((String)typeOfService.getSelectedItem());
	}

	/**
	 * Update GUI from a changed configuration bean (perhaps by undo/redo).
	 * 
	 */
	@Override
	public void refreshConfiguration() {
		configBean = activity.getConfiguration();
		
		// FIXME: Update UI elements from your bean fields
		urlField.setText(configBean.getPdlDescriptionFile());
		if(configBean.getServiceType()==null || configBean.getServiceType().isEmpty())
			configBean.setServiceType(configBean.PDLSERVICE);
		typeOfService.setSelectedItem(configBean.getServiceType());

	}
}
