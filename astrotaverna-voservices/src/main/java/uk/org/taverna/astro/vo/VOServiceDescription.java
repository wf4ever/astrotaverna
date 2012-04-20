package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;

public class VOServiceDescription extends ServiceDescription<RESTActivityConfigurationBean> {

	private String accessURL;
	private String name;
	private URI identifier;
	
	
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
		return urlSig + "ra={ra}&dec={dec}&sr={sr}";
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
		this.name = name;
	}

	public URI getIdentifier() {
		return identifier;
	}

	public void setIdentifier(URI identifier) {
		this.identifier = identifier;
	}


}
