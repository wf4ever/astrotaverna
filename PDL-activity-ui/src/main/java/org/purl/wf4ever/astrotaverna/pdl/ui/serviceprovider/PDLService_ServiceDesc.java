package org.purl.wf4ever.astrotaverna.pdl.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;


import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.pdl.ui.serviceprovider.PDLServiceIcon;

public class PDLService_ServiceDesc extends ServiceDescription<PDLServiceActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<PDLServiceActivityConfigurationBean>> getActivityClass() {
		return PDLServiceActivity.class;
	} 

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public PDLServiceActivityConfigurationBean getActivityConfiguration() {
		PDLServiceActivityConfigurationBean bean = new PDLServiceActivityConfigurationBean();
		bean.setPdlDescriptionFile("http://www.exampleuri.com/pdldescriptionfile.xml");
//		bean.setServiceType(bean.PDLSERVICE);
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return PDLServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return "PDL service";//exampleString;
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		//return Arrays.asList("Stilts -" + exampleUri);
		//return Arrays.asList("Stilts" + this.getName());
		//return Arrays.asList("Astro local services", "Stilts");
		return Arrays.asList("Astro tools");
	}

	/**
	 * Return a list of data values uniquely identifying this service
	 * description (to avoid duplicates). Include only primary key like fields,
	 * ie. ignore descriptions, icons, etc.
	 */
	@Override
	protected List<? extends Object> getIdentifyingData() {
		// FIXME: Use your fields instead of example fields
		//return Arrays.<Object>asList(exampleString, exampleUri);
		return Arrays.<Object>asList("PDL", "astro-iaa", this.getName());
	}
	
	/**
	 * This method makes that the configuration panel appears when you add the service
	 * from the service panel to the workflow design area
	 */
	@Override
	public boolean isTemplateService() {
		return true;
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	
	
	private String pdlDescriptionFile;


	public String getPdlDescriptionFile() {
		return pdlDescriptionFile;
	}

	public void setPdlDescriptionFile(String pdlDescriptionFile) {
		this.pdlDescriptionFile = pdlDescriptionFile;
	}
	


	/*
	public String getTypeOfFilter() {
		return typeOfFilter;
	}

	public void setTypeOfFilter(String typeOfFilter) {
		this.typeOfFilter = typeOfFilter;
	}
	*/

	
	//private String exampleString;
	//private URI exampleUri;
	
	//public String getExampleString() {
	//	return exampleString;
	//}
	//public URI getExampleUri() {
	//	return exampleUri;
	//}
	//public void setExampleString(String exampleString) {
	//	this.exampleString = exampleString;
	//}
	//public void setExampleUri(URI exampleUri) {
	//	this.exampleUri = exampleUri;
	//}


}
