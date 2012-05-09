package org.purl.wf4ever.astrotaverna.tjoin;

import java.io.File;
import java.io.Serializable;
//import java.net.URI;

/**
 * Stilts activity configuration bean.
 * 
 */
public class TjoinActivityConfigurationBean implements Serializable {

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
	private int numberOfTables;
	
	private String cmd;
	
	private String inputFormat;
	
	//private String shortDescription;
	
	//private String longDescription; 

	public int getNumberOfTables() {
		return numberOfTables;
	}

	public void setNumberOfTables(int numberOfTables) {
		this.numberOfTables = numberOfTables;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	//public String getShortDescription() {
	//	return shortDescription;
	//}

	//public void setShortDescription(String shortDescription) {
	//	this.shortDescription = shortDescription;
	//}

	//public String getLongDescription() {
	//	return longDescription;
	//}

	//public void setLongDescription(String longDescription) {
	//	this.longDescription = longDescription;
	//}
	
	



	//private URI exampleUri;
	//public void setExampleUri(URI exampleUri) {
	//	this.exampleUri = exampleUri;
	//}

}
