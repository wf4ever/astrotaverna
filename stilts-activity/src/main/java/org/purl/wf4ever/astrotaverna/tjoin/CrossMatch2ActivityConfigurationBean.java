package org.purl.wf4ever.astrotaverna.tjoin;

import java.io.File;
import java.io.Serializable;
//import java.net.URI;

/**
 * Activity Configuration bean
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class CrossMatch2ActivityConfigurationBean implements Serializable {

	/*
	 * TODO: Remove this comment.
	 * 
	 * The configuration specifies the variable options and configurations for
	 * an activity that has been added to a workflow. For instance for a WSDL
	 * activity, the configuration contains the URL for the WSDL together with
	 * the method name. String constant configurations contain the string that
	 * is to be returned, while Beanshell script configurations contain both the
	 * scripts and the input/output ports (by subclassing
	 * ActivityPortsDefinitionBean).
	 * 
	 * Configuration beans are serialised as XML (currently by using XMLBeans)
	 * when Taverna is saving the workflow definitions. Therefore the
	 * configuration beans need to follow the JavaBeans style and only have
	 * fields of 'simple' types such as Strings, integers, etc. Other beans can
	 * be referenced as well, as long as they are part of the same plugin.
	 */
	
	// TODO: Remove the example fields and getters/setters and add your own	
	
	private String typeOfInput;
	private String matchCriteria;
	private String join;
	private String fixcols;
	private String find;
	private boolean showScoreCol;
	

	public CrossMatch2ActivityConfigurationBean(){
		typeOfInput = "String";
		matchCriteria = "sky";
		join = "1and2";
		fixcols = "dups";
		find = "best";
		showScoreCol = false;
	}


	public boolean getShowScoreCol() {
		return showScoreCol;
	}

	public void setShowScoreCol(boolean showScoreCol) {
		this.showScoreCol = showScoreCol;
	}

	public String getFind() {
		return find;
	}

	public void setFind(String find) {
		this.find = find;
	}

	public String getMatchCriteria() {
		return matchCriteria;
	}

	public void setMatchCriteria(String matchCriteria) {
		this.matchCriteria = matchCriteria;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getFixcols() {
		return fixcols;
	}

	public void setFixcols(String fixcols) {
		this.fixcols = fixcols;
	}

	public String getTypeOfInput() {
		return typeOfInput;
	}

	public void setTypeOfInput(String typeOfInput) {
		this.typeOfInput = typeOfInput;
	}

	

	

}
