package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

public class VOServiceDescription extends
		ServiceDescription<RESTActivityConfigurationBean> {

	private Map<String, String> parameters = new HashMap<String, String>();
	private URI identifier;
	private String name;
	private String searchType;
	private String urlSignature;
	private String accessURL;

	public String getAccessURL() {
		return accessURL;
	}

	@Override
	public Class<RESTActivity> getActivityClass() {
		return RESTActivity.class;
	}

	@Override
	public RESTActivityConfigurationBean getActivityConfiguration() {
		RESTActivityConfigurationBean configurationBean = RESTActivityConfigurationBean
				.getDefaultInstance();
		configurationBean.setUrlSignature(getUrlSignature());
		configurationBean
				.setAcceptsHeaderValue("application/x-votable+xml, text/xml;content=x-votable, text/xml;votable;q=0.7,  application/xml;q=0.5, text/xml;q=0.6");
		return configurationBean;
	}

	@Override
	public Icon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	public URI getIdentifier() {
		return identifier;
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		return Arrays.asList(getIdentifier());
	}

	@Override
	public String getName() {
		return name;
	}

	public Map<String, String> getParameterValues() {
		return parameters;
	}

	@Override
	public List<String> getPath() {
		return Arrays.asList("VO services", getName());
	}

	public String getSearchType() {
		return searchType;
	}

	public String getUrlSignature() {
		return urlSignature;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}

	public void setName(String name) {
		this.name = Tools.sanitiseName(name);
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setUrlSignature(String urlSignature) {
		this.urlSignature = urlSignature;
	}

	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}

}
