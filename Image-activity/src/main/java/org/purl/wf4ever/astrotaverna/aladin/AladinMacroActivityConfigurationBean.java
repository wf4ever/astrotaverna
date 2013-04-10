package org.purl.wf4ever.astrotaverna.aladin;

import java.io.File;
import java.io.Serializable;
//import java.net.URI;

/**
 * Activity Configuration bean
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class AladinMacroActivityConfigurationBean implements Serializable {

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
	private String typeOfMode;
	

	public String getTypeOfInput() {
		return typeOfInput;
	}

	public void setTypeOfInput(String typeOfInput) {
		this.typeOfInput = typeOfInput;
	}
	
	public String getTypeOfMode() {
		return typeOfMode;
	}

	public void setTypeOfMode(String typeOfProcess) {
		this.typeOfMode = typeOfProcess;
	}
	

}
