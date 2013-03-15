package org.purl.wf4ever.astrotaverna.query.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;



import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import org.purl.wf4ever.astrotaverna.query.ui.serviceprovider.VOQueryServiceIcon;


public class SSAServiceDesc extends ServiceDescription<RESTActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<RESTActivityConfigurationBean>> getActivityClass() {
		return RESTActivity.class;
	} 

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public RESTActivityConfigurationBean getActivityConfiguration() {
		////RESTActivityConfigurationBean bean = new RESTActivityConfigurationBean();
		//RESTActivityConfigurationBean bean = RESTActivityConfigurationBean.getDefaultInstance();
		////bean.setType();
		//return bean;
		RESTActivityConfigurationBean configurationBean = RESTActivityConfigurationBean
				.getDefaultInstance();
		configurationBean.setUrlSignature("http://www.exampleuri.com/CS&RA={RA}&DEC={DEC}&SR={SR}");
		configurationBean
				.setAcceptsHeaderValue("application/x-votable+xml, text/xml;content=x-votable, text/xml;votable;q=0.7,  application/xml;q=0.5, text/xml;q=0.6");
		return configurationBean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return VOQueryServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return "Access SSA VOService";//exampleString;
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
		return Arrays.asList("Astro tools", "Services");
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
		return Arrays.<Object>asList("Query", "astro-iaa", this.getName());
	}

	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	
	/**
	 * This method makes that the configuration panel appears when you add the service
	 * from the service panel to the workflow design area
	 */
	@Override
	public boolean isTemplateService() {
		return true;
	}
	
	/*
	public void setUrlSignature(String urlSignature) {
		this.urlSignature = urlSignature;
	}

	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}
	
	public String getUrlSignature() {
		return urlSignature;
	}
	
	public String getAccessURL() {
		return accessURL;
	}

	private String urlSignature;
	private String accessURL;
	*/
}
