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

import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivityConfigurationBean;
import javax.swing.JSpinner;

@SuppressWarnings("serial")
public class StiltsConfigurationPanel
		extends
		ActivityConfigurationPanel<TjoinActivity, 
        TjoinActivityConfigurationBean> {

	private TjoinActivity activity;
	private TjoinActivityConfigurationBean configBean;
	
	String[] inputTypesStrings = {"File", "URL", "String"};
	//String[] filterTypesStrings = {"Column names", "UCDs"};
	
	private JComboBox  typeOfInput;

	public StiltsConfigurationPanel(TjoinActivity activity) {
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
		
		//JLabel labelCmd = new JLabel("Additional commands:");
		//add(labelCmd);
		//fieldCmd = new JTextField(25);
		//add(fieldCmd);
		//labelCmd.setLabelFor(fieldCmd);

		

		// Populate fields from activity configuration bean
		refreshConfiguration();
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
		//THIS MUST BE ADDAPTED TO THE TJOIN REQUIREMENS.
		String errorMessage=null;
		
		String  tinput = (String)typeOfInput.getSelectedItem();
		if(!(      tinput.compareTo("File")==0
				|| tinput.compareTo("URL")==0
				|| tinput.compareTo("String")==0)){
			//"Invalid input type

			errorMessage = "Valid inputs: file, url or string.";
			
		}
		
		
		//try{ 
		//	int number = Integer.parseInt(fieldNTables.getText());
		//	if(number<2 || number >4){
		//		if(errorMessage!=null){
		//			errorMessage += "\nThe number of files shoud be between 2 and 4";
		//		}else{
		//			errorMessage = "\nThe number of files shoud be between 2 and 4";
		//		}
		//	}
		//}catch (NumberFormatException ex){
		//	if(errorMessage!=null){
		//		errorMessage += "\nThe number of files is a number between 2 and 4";
		//	}else{
		//		errorMessage = "\nThe number of files is a number between 2 and 4";
		//	}
		//}
		
		if (errorMessage!=null){
			JOptionPane.showMessageDialog(this, errorMessage,
					"Invalid Format", JOptionPane.ERROR_MESSAGE);
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
	public TjoinActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		String originalTypeOfInput = configBean.getTypeOfInput();
		
		return ! (originalTypeOfInput.equals((String)typeOfInput.getSelectedItem()));
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new TjoinActivityConfigurationBean();
		
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
