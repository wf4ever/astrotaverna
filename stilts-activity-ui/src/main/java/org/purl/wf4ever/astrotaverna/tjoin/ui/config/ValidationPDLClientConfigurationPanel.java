package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import org.apache.log4j.Logger;
import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;
import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.view.votable.VOTableRenderer;

@SuppressWarnings("serial")
public class ValidationPDLClientConfigurationPanel
		extends
		ActivityConfigurationPanel<ValidationPDLClientActivity, 
        ValidationPDLClientActivityConfigurationBean> {

	
	private ValidationPDLClientActivity activity;
	private ValidationPDLClientActivityConfigurationBean configBean;
	
	//String[] inputTypesStrings = {"File", "URL", "String"};
	//String[] filterTypesStrings = {"Column names", "UCDs"};
	
	private JTextField  urlField;
	//private JComboBox typeOfFilter;


	public ValidationPDLClientConfigurationPanel(ValidationPDLClientActivity activity) {
		this.activity = activity;
		initGui();
	}

	protected void initGui() {
		removeAll();
		setLayout(new GridLayout(0, 2));

		// FIXME: Create GUI depending on activity configuration bean
		JLabel labelString = new JLabel("PDL-description URL:");
		add(labelString);
		urlField = new JTextField("");//http://www.exampleuri.com/pdldescriptionfile.xml
		add(urlField);
		labelString.setLabelFor(urlField);
			
		// Populate fields from activity configuration bean
		refreshConfiguration();
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
		
		String errorMessage=null;
		
		String  tinput = urlField.getText();
				
		
		if (errorMessage!=null){
			JOptionPane.showMessageDialog(this, errorMessage,
					"Invalid configuration", JOptionPane.ERROR_MESSAGE);
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
	public ValidationPDLClientActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		String originalTypeOfInput = configBean.getTypeOfInput();
		//String originalTypeOfFilter = configBean.getTypeOfFilter();
		// true (changed) unless all fields match the originals
		
		return ! (originalTypeOfInput.equals((String)typeOfInput.getSelectedItem())
				/*&& originalTypeOfFilter.equals((String)typeOfFilter.getSelectedItem())*/ );
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new ValidationPDLClientActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setTypeOfInput((String)typeOfInput.getSelectedItem());
		//configBean.setTypeOfFilter((String)typeOfFilter.getSelectedItem());
		
	}

	/**
	 * Update GUI from a changed configuration bean (perhaps by undo/redo).
	 * 
	 */
	@Override
	public void refreshConfiguration() {
		configBean = activity.getConfiguration();
		
		// FIXME: Update UI elements from your bean fields
		typeOfInput.setSelectedItem(configBean.getTypeOfInput());
		//typeOfFilter.setSelectedItem(configBean.getTypeOfFilter());

	}
}
