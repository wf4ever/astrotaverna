package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.GridLayout;
import java.net.URI;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivityConfigurationBean;
import javax.swing.JSpinner;

@SuppressWarnings("serial")
public class TemplateFillerConfigurationPanel
		extends
		ActivityConfigurationPanel<TemplateFillerActivity, 
        TemplateFillerActivityConfigurationBean> {

	private TemplateFillerActivity activity;
	private TemplateFillerActivityConfigurationBean configBean;
	
	String[] inputTypesStrings = {"File", "URL", "String"};
	
	private JComboBox  typeOfInput;


	public TemplateFillerConfigurationPanel(TemplateFillerActivity activity) {
		this.activity = activity;
		initGui();
	}

	protected void initGui() {
		removeAll();
		setLayout(new GridLayout(0, 2));

		// FIXME: Create GUI depending on activity configuration bean
		JLabel labelString = new JLabel("Input type:");
		add(labelString);
		typeOfInput = new JComboBox(inputTypesStrings);
		add(typeOfInput);
		labelString.setLabelFor(typeOfInput);
		typeOfInput.setSelectedIndex(1);
		
			
		// Populate fields from activity configuration bean
		refreshConfiguration();
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
		//THIS MUST BE ADDAPTED TO THE TPIPE REQUIREMENS.
		String errorMessage=null;
		
		String  tinput = (String)typeOfInput.getSelectedItem();
		if(!(      tinput.compareTo("File")==0
				|| tinput.compareTo("URL")==0
				|| tinput.compareTo("String")==0)){
			//"Invalid input type

			errorMessage = "Valid inputs: file, url or string.";
			
		}
		
		
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
	public TemplateFillerActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		String originalTypeOfInput = configBean.getTypeOfInput();
		// true (changed) unless all fields match the originals
		
		return ! (originalTypeOfInput.equals((String)typeOfInput.getSelectedItem()));
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new TemplateFillerActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setTypeOfInput((String)typeOfInput.getSelectedItem());
		
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
	}
}
