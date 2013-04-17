package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.purl.wf4ever.astrotaverna.voutils.AddCommonRowToVOTableActivity;
import org.purl.wf4ever.astrotaverna.voutils.AddCommonRowToVOTableActivityConfigurationBean;


import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;



@SuppressWarnings("serial")
public class AddCommonRowToVOTableConfigurationPanel
		extends
		ActivityConfigurationPanel<AddCommonRowToVOTableActivity, 
        AddCommonRowToVOTableActivityConfigurationBean> {

	private AddCommonRowToVOTableActivity activity;
	private AddCommonRowToVOTableActivityConfigurationBean configBean;
	
	String[] inputTypesStrings = {"File", "URL", "String"};
	String[] commonRowPositionStrings = {"Left", "Right"};
	
	private JComboBox  typeOfInput;
	private JComboBox commonRowPosition;


	public AddCommonRowToVOTableConfigurationPanel(AddCommonRowToVOTableActivity activity) {
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
		
		labelString = new JLabel("Common row position:");
		add(labelString);
		commonRowPosition = new JComboBox(commonRowPositionStrings);
		add(commonRowPosition);
		labelString.setLabelFor(commonRowPosition);
		commonRowPosition.setSelectedIndex(1);

			
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
		
		String  tfilter = (String)commonRowPosition.getSelectedItem();
		if(!(      tfilter.compareTo("Left")==0
				|| tfilter.compareTo("Right")==0)){
			
			errorMessage = "Invalid position: Left or Right";
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
	public AddCommonRowToVOTableActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		String originalTypeOfInput = configBean.getTypeOfInput();
		String originalPosition = configBean.getCommonRowPosition();
		// true (changed) unless all fields match the originals
		
		return ! (originalTypeOfInput.compareTo((String)typeOfInput.getSelectedItem())==0
				&& originalPosition.compareTo((String)commonRowPosition.getSelectedItem())==0 );
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new AddCommonRowToVOTableActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setTypeOfInput((String)typeOfInput.getSelectedItem());
		configBean.setCommonRowPosition((String)commonRowPosition.getSelectedItem());
		
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
		commonRowPosition.setSelectedItem(configBean.getCommonRowPosition());

	}
}
