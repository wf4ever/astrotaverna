package org.purl.wf4ever.astrotaverna.image.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivity;
import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivityConfigurationBean;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;


public class AladinMacroServiceDesc extends ServiceDescription<AladinMacroActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<AladinMacroActivityConfigurationBean>> getActivityClass() {
		return AladinMacroActivity.class;
	} 

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public AladinMacroActivityConfigurationBean getActivityConfiguration() {
		AladinMacroActivityConfigurationBean bean = new AladinMacroActivityConfigurationBean();
		bean.setTypeOfInput("String");
		bean.setTypeOfMode("nogui");
		//bean.setTypeOfFilter("Column names");
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return ImageServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return "Use Aladin macro";//exampleString;
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
		return Arrays.<Object>asList("aladin", "astro-iaa", this.getName());
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	
	
	private String typeOfInput;
	private String typeOfMode;
	
	//private String typeOfFilter;

	public String getTypeOfInput() {
		return typeOfInput;
	}

	public String getTypeOfMode() {
		return typeOfMode;
	}

	public void setTypeOfMode(String typeOfMode) {
		this.typeOfMode = typeOfMode;
	}

	public void setTypeOfInput(String typeOfInput) {
		this.typeOfInput = typeOfInput;
	}


}
