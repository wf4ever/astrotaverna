package uk.org.taverna.astro.vo;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.activities.rest.RESTActivity;
import net.sf.taverna.t2.activities.rest.RESTActivityConfigurationBean;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;

public class VOServiceDescription extends ServiceDescription<RESTActivityConfigurationBean> {

	@Override
	public Class<RESTActivity> getActivityClass() {
		return RESTActivity.class;
	}

	@Override
	public RESTActivityConfigurationBean getActivityConfiguration() {
		RESTActivityConfigurationBean configurationBean = new RESTActivityConfigurationBean();
		configurationBean.setUrlSignature("http://www.example.com/{fred}/{soup}.xml");
		return configurationBean;
	}

	@Override
	public Icon getIcon() {
		return VOServicesPerspective.voIcon;
	}

	@Override
	public String getName() {
		return "dummyService";
	}

	@Override
	public List<String> getPath() {
		return Arrays.asList("VO services", "Registry X");
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		return Arrays.asList("VO services", "Registry X");
	}

}
