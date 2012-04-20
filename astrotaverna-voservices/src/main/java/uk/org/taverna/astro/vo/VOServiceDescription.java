package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

import org.apache.log4j.Logger;

public class VOServiceDescription extends ServiceDescription<RESTActivityConfigurationBean> {
	private static Logger logger = Logger.getLogger(VOServiceDescription.class);
	
	private String accessURL;
	private String name;
	private URI identifier;
	private String searchType;
	
	
	public String getSearchType() {
		return searchType;
	}

	@Override
	public Class<RESTActivity> getActivityClass() {
		return RESTActivity.class;
	}

	@Override
	public RESTActivityConfigurationBean getActivityConfiguration() {
		RESTActivityConfigurationBean configurationBean = RESTActivityConfigurationBean.getDefaultInstance();
		configurationBean.setUrlSignature(transformAccessURL());		
		return configurationBean;
	}

	public String transformAccessURL() {
		String urlSig = getAccessURL();
		if (! urlSig.contains("?")) {
			urlSig += "?";
		} else if (! urlSig.endsWith("&")) {
			urlSig += "&";
		}
		// TODO: Work out parameters from service type
		
		if ("ConeSearch".equals(getSearchType())) {
			return urlSig + "ra={ra}&dec={dec}&sr={sr}";
		} else if("SimpleSpectralAccess".equals(getSearchType())) {
			// TODO parameters for SSA
			return urlSig;
		} else if("SimpleImageAccess".equals(getSearchType())) {
			// TODO parameters for SIA
			return urlSig; 
		} else {
			logger.warn("Unknown search type " + getSearchType() + " in " + getIdentifier());
			return urlSig;
		}
	}

	public String getAccessURL() {
		return accessURL;
	}

	@Override
	public Icon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getPath() {
		return Arrays.asList("VO services", getName());
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		return Arrays.asList(getIdentifier());
	}

	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}

	public void setName(String name) {
		this.name = Tools.sanitiseName(name);
	}

	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;	
	}


}
