package org.purl.wf4ever.astrotaverna.tjoin.ui.serviceprovider;


import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivity;
import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivityConfigurationBean;

public class GetListFromColumnServiceDesc extends ServiceDescription<GetListFromColumnActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<GetListFromColumnActivityConfigurationBean>> getActivityClass() {
		return GetListFromColumnActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public GetListFromColumnActivityConfigurationBean getActivityConfiguration() {
		GetListFromColumnActivityConfigurationBean bean = new GetListFromColumnActivityConfigurationBean();
		bean.setTypeOfInput("String");
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return StiltsServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return "List from column";//exampleString;
	}
	
	public String getIdName() {
		return "List from column";//exampleString;
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
		return Arrays.<Object>asList("stilts", "astro-iaa", this.getIdName());
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	
	
	private String typeOfInput;
	

	public String getTypeOfInput() {
		return typeOfInput;
	}

	public void setTypeOfInput(String typeOfInput) {
		this.typeOfInput = typeOfInput;
	}



	
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
