package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.GridLayout;
import java.net.URI;

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
	
	private JTextField fieldFormat;
	//private JTextField fieldCmd;
	//private JTextField fieldNTables;
	private JSpinner fieldNTables;
	//ADDITIONAL FIELD FOR NUMBER OF TABLES

	public StiltsConfigurationPanel(TjoinActivity activity) {
		this.activity = activity;
		initGui();
	}

	protected void initGui() {
		removeAll();
		setLayout(new GridLayout(0, 2));

		// FIXME: Create GUI depending on activity configuration bean
		JLabel labelString = new JLabel("Files format:");
		add(labelString);
		fieldFormat = new JTextField(20);
		add(fieldFormat);
		labelString.setLabelFor(fieldFormat);

		JLabel labelNTables = new JLabel("Number of files:");
		add(labelNTables);
		//fieldNTables = new JTextField(25);
		int min = 2; int max = 4; int step = 1; int initValue = 2;
		SpinnerModel model = new SpinnerNumberModel(initValue, min, max, step);
		fieldNTables = new JSpinner(model);
		add(fieldNTables);
		labelNTables.setLabelFor(fieldNTables);
		
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
		
		String format = fieldFormat.getText();
		if(!(format.compareTo("fits")==0 
				|| format.compareTo("colfits")==0
				|| format.compareTo("votable")==0
				|| format.compareTo("ascii")==0
				|| format.compareTo("csv")==0
				|| format.compareTo("tst")==0
				|| format.compareTo("ipac")==0)){
					//"Invalid format for the input tables");

			errorMessage = "Valid formats: colfits, votable, csv, tst, tst, ipac";
			
		}
		
		//int number = (Integer)((SpinnerNumberModel)fieldNTables.getModel()).getValue();
		int number = ((Integer)((SpinnerNumberModel)fieldNTables.getModel()).getValue()).intValue();
		
		if(number<2 || number >4){
			if(errorMessage!=null){
				errorMessage += "\nThe number of files shoud be between 2 and 4";
			}else{
				errorMessage = "\nThe number of files shoud be between 2 and 4";
			}
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
		String originalFormat = configBean.getInputFormat();
		String originalCmd = configBean.getCmd();
		int originalNTables = configBean.getNumberOfTables();
		// true (changed) unless all fields match the originals
		
		return ! (originalFormat.equals(fieldFormat.getText())
//				&& originalCmd.equals(fieldCmd.getText()) 
				&& originalNTables == ((Integer)((SpinnerNumberModel)fieldNTables.getModel()).getValue()).intValue());
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration(){
		configBean = new TjoinActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setInputFormat(fieldFormat.getText());
		//configBean.setCmd(fieldCmd.getText());
		configBean.setNumberOfTables(((Integer)((SpinnerNumberModel)fieldNTables.getModel()).getValue()).intValue());
		
	}

	/**
	 * Update GUI from a changed configuration bean (perhaps by undo/redo).
	 * 
	 */
	@Override
	public void refreshConfiguration() {
		configBean = activity.getConfiguration();
		
		// FIXME: Update UI elements from your bean fields
		fieldFormat.setText(configBean.getInputFormat());
		//fieldCmd.setText(configBean.getCmd());
		((SpinnerNumberModel)fieldNTables.getModel()).setValue(new Integer(configBean.getNumberOfTables()));
	}
}
