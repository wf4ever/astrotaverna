package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import org.purl.wf4ever.astrotaverna.vorepo.VORepository;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.servicedescriptions.AbstractConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ConfigurableServiceProvider;
import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;
import uk.org.taverna.astro.wsdl.registrysearch.ErrorResp;

public class VOServiceProvider extends
		AbstractConfigurableServiceProvider<VOServiceProviderConfig> implements
		ServiceDescriptionProvider,
		ConfigurableServiceProvider<VOServiceProviderConfig> {

	private static final URI providerId = URI
			.create("http://wf4ever.github.com/astrotaverna#serviceProvider");

	private VOServicesController controller;

	private VOServicesModel model;

	public VOServiceProvider() {
		super(new VOServiceProviderConfig());
	}

	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		VORepository repo = getModel().getRepository();
		List<Service> services;
		try {
			services = repo.resourceSearch(ConeSearch.class);
		} catch (ErrorResp ex) {
			callBack.fail("Could not do cone search", ex);
			return;
		}
		List<ServiceDescription> results = new ArrayList<ServiceDescription>();
		for (Service s : services) {
			VOServiceDescription desc;
			desc = getController().makeServiceDescription(s, ConeSearch.class);
			results.add(desc);
		}

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

		// No more results will be coming
		callBack.finished();
	}

	public VOServicesController getController() {
		if (controller == null) {
			controller = new VOServicesController();
			controller.setModel(getModel());
		}
		return controller;
	}

	@Override
	public List<VOServiceProviderConfig> getDefaultConfigurations() {
		return new ArrayList<VOServiceProviderConfig>();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return VOServiceIcon.voIcon;
	}

	public String getId() {
		return providerId.toASCIIString();
	}

	@Override
	protected List<? extends Object> getIdentifyingData() {
		return Arrays.asList(getConfiguration().getEndpoint());
	}

	public VOServicesModel getModel() {
		if (model == null) {
			model = new VOServicesModel();
			model.setController(getController());
		}
		return model;
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "VO services";
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return getName();
	}

}
