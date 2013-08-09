package org.purl.wf4ever.astrotaverna.pdl;

import java.io.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
//import java.net.URI;


/**
 * Activity Configuration bean
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class PDLServiceActivityConfigurationBean implements Serializable {

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
	
	private String pdlDescriptionFile;
	private String serviceType = "PDLserver";
	
	final public String PDLSERVICE = "PDLserver";
	final public String RESTSERVICE = "Rest";
	final public String VOTABLERESTSERVICE = "smartVORest";
	
	// only need to store the configuration of inputs and outputs, as all of them are dynamic;
	// only inputs that constitute components of URL signature are to be stored
	//private Map<String, Class<?>> activityInputs;
	//private Map<String, Class<?>> activityOutputs;
	
	/*
	public void setActivityInputs(Map<String, Class<?>> activityInputs) {
		this.activityInputs = activityInputs;
	}

	public Map<String, Class<?>> getActivityInputs() {
		return activityInputs;
	}
	
	public void setActivityOutputs(Map<String, Class<?>> activityOutputs) {
		this.activityOutputs = activityOutputs;
	}

	public Map<String, Class<?>> getActivityOutputs() {
		return activityOutputs;
	}
	*/

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getPdlDescriptionFile() {
		return pdlDescriptionFile;
	}

	public void setPdlDescriptionFile(String pdlDescriptionFile) {
		this.pdlDescriptionFile = pdlDescriptionFile;
	}
	
	

}
