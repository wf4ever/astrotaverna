package org.purl.wf4ever.astrotaverna.tjoin.ui.config;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;

import org.purl.wf4ever.astrotaverna.tjoin.CrossMatch2Activity;
import org.purl.wf4ever.astrotaverna.tjoin.CrossMatch2ActivityConfigurationBean;


import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;


@SuppressWarnings("serial")
public class CrossMatch2ConfigurationPanel
		extends
		ActivityConfigurationPanel<CrossMatch2Activity, 
        CrossMatch2ActivityConfigurationBean> {

	private CrossMatch2Activity activity;
	private CrossMatch2ActivityConfigurationBean configBean;
	
	String[] inputTypesOptions = {"File", "URL", "String"};
	private String [] matchCriteriaOptions = {"sky", "skyerr", "skyellipse", "sky3d", "exact", "1d", "2d", "2d_anisotropic", "1d_err", "2d_err", "2d_ellipse", "other"};
	private String [] joinOptions = {"1and2", "1or2", "all1", "all2", "1not2", "2not1", "1xor2"};
	private String [] fixcolsOptions = {"none", "dups", "all"};
	private String [] findOptions = {"all", "best", "best1", "best2"};
	private boolean showScoreCol;
	//String[] filterTypesStrings = {"Column names", "UCDs"};
	
	private JComboBox typeOfInput;
	private JComboBox typeOfMatch;
	private JComboBox typeOfJoin;
	private JComboBox typeOfFixcols;
	private JComboBox typeOfFind;
	private JCheckBox showScoreColBox;
	
	//private JComboBox typeOfFilter;


	public CrossMatch2ConfigurationPanel(CrossMatch2Activity activity) {
		this.activity = activity;
		initGui();
	}

	protected void initGui() {
		removeAll();
		setLayout(new GridLayout(6, 2));

		// FIXME: Create GUI depending on activity configuration bean
		JLabel labelString = new JLabel("Input type:");
		add(labelString);
		typeOfInput = new JComboBox(inputTypesOptions);
		add(typeOfInput);
		labelString.setLabelFor(typeOfInput);
		typeOfInput.setSelectedIndex(1);
		
		labelString = new JLabel("Match type:");
		add(labelString);
		typeOfMatch = new JComboBox(matchCriteriaOptions);
		add(typeOfMatch);
		labelString.setLabelFor(typeOfMatch);
		typeOfMatch.setSelectedIndex(1);

		labelString = new JLabel("Join type:");
		add(labelString);
		typeOfJoin = new JComboBox(joinOptions);
		add(typeOfJoin);
		labelString.setLabelFor(typeOfJoin);
		typeOfJoin.setSelectedIndex(1);


		labelString = new JLabel("Fixcols type:");
		add(labelString);
		typeOfFixcols = new JComboBox(fixcolsOptions);
		add(typeOfFixcols);
		labelString.setLabelFor(typeOfFixcols);
		typeOfFixcols.setSelectedIndex(1);
		
		labelString = new JLabel("Find type:");
		add(labelString);
		typeOfFind = new JComboBox(findOptions);
		add(typeOfFind);
		labelString.setLabelFor(typeOfFind);
		typeOfFind.setSelectedIndex(1);
		
		labelString = new JLabel("Show score col:");
		add(labelString);
		showScoreColBox = new JCheckBox();
		add(showScoreColBox);
		labelString.setLabelFor(showScoreColBox);
			
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
		
		String  value = (String)typeOfInput.getSelectedItem();
		if(!(      value.compareTo("File")==0
				|| value.compareTo("URL")==0
				|| value.compareTo("String")==0)){
			//"Invalid input type

			errorMessage = "Valid inputs: file, url or string.";
			
		}
		
		value = (String)typeOfMatch.getSelectedItem();
		if(!(value.compareTo("sky")==0
				|| value.compareTo("skyerr")==0
				|| value.compareTo("skyellipse")==0
				|| value.compareTo("sky3d")==0
				|| value.compareTo("exact")==0
				|| value.compareTo("1d")==0
				|| value.compareTo("2d")==0
				|| value.compareTo("2d_anisotropic")==0
				|| value.compareTo("1d_err")==0
				|| value.compareTo("2d_err")==0
				|| value.compareTo("2d_ellipse")==0
				|| value.compareTo("other")==0)){

			errorMessage = "Valid values for Match: sky, skyerr, skyellipse, sky3d, exact, 1d, 2d, 2d_anisotropic, 1d_err, 2d_err, 2d_ellipse, other";
			
		}
		
		value = (String)typeOfJoin.getSelectedItem();
		if(!(value.compareTo("1and2")==0
				|| value.compareTo("1or2")==0
				|| value.compareTo("all1")==0
				|| value.compareTo("all2")==0
				|| value.compareTo("1not2")==0
				|| value.compareTo("2not1")==0
				|| value.compareTo("1xor2")==0)){
			//"Invalid input type

			errorMessage = "Valid values for join: 1and2, 1or2, all1, all2, 1not2, 2not1, 1xor2";
			
		}
		
		value = (String)typeOfFixcols.getSelectedItem();
		if(!(      value.compareTo("none")==0
				|| value.compareTo("dups")==0
				|| value.compareTo("all")==0)){
			//"Invalid input type

			errorMessage = "Valid values for Fixcols: none, dups, all";
			
		}
		
		value = (String)typeOfFind.getSelectedItem();
		if(!(      value.compareTo("all")==0
				|| value.compareTo("best")==0
				|| value.compareTo("best1")==0
				|| value.compareTo("best2")==0)){
			//"Invalid input type

			errorMessage = "Valid values for  find: all, best, best1, best2";
			
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
	public CrossMatch2ActivityConfigurationBean getConfiguration() {
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
		configBean = new CrossMatch2ActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setTypeOfInput((String)typeOfInput.getSelectedItem());
		configBean.setMatchCriteria((String)typeOfMatch.getSelectedItem());
		configBean.setJoin((String)typeOfJoin.getSelectedItem());
		configBean.setFixcols((String)typeOfFixcols.getSelectedItem());
		configBean.setFind((String)typeOfFind.getSelectedItem());
		configBean.setShowScoreCol(this.showScoreColBox.isSelected());
		
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
		typeOfMatch.setSelectedItem(configBean.getMatchCriteria());
		typeOfJoin.setSelectedItem(configBean.getJoin());
		typeOfFixcols.setSelectedItem(configBean.getFixcols());
		typeOfFind.setSelectedItem(configBean.getFind());
		showScoreColBox.setSelected(configBean.getShowScoreCol());

	}
}
