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

public class VOServiceDescription extends
		ServiceDescription<RESTActivityConfigurationBean> {
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
		RESTActivityConfigurationBean configurationBean = RESTActivityConfigurationBean
				.getDefaultInstance();
		configurationBean.setUrlSignature(transformAccessURL());
		configurationBean.setAcceptsHeaderValue("application/x-votable+xml, text/xml;content=x-votable, application/xml;q=0.5, text/xml;q=0.6");
		return configurationBean;
	}

	// TODO: Move to controller?
	public String transformAccessURL() {
		String urlSig = getAccessURL();
		if (!urlSig.contains("?")) {
			urlSig += "?";
		} else if (!urlSig.endsWith("&")) {
			urlSig += "&";
		}
		// Note: Only mandatory parameters (except REQUEST) 
		// are included here
		if ("ConeSearch".equals(getSearchType())) {
			// http://www.ivoa.net/Documents/latest/ConeSearch.html
			return urlSig + "RA={RA}&DEC={DEC}&SR={SR}";
		} else if ("SimpleSpectralAccess".equals(getSearchType())) {
			// http://www.ivoa.net/Documents/SSA/20120210/index.html
			return urlSig + "POS={POS}&SIZE={SIZE}&TIME={TIME}&BAND={BAND}";
		} else if ("SimpleImageAccess".equals(getSearchType())) {
			// http://www.ivoa.net/Documents/SIA/20091116/
			return urlSig + "POS={POS}&SIZE={SIZE}";
		} else if ("SimpleLineAccess".equals(getSearchType())) {
			// http://www.ivoa.net/Documents/SLAP/20101209/index.html
			return urlSig + "WAVELENGTH={WAVELENGTH}";
		} else {
			logger.warn("Unknown search type " + getSearchType() + " in "
					+ getIdentifier());
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
