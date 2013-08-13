package org.purl.wf4ever.astrotaverna.vo;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.purl.wf4ever.astrotaverna.vorepo.VORepository;
import org.purl.wf4ever.astrotaverna.wsdl.registrysearch.ErrorResp;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.slap.v0.SimpleLineAccess;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Service;

public class VOServicesModel {
	private static List<Service> EMPTY_SERVICES = Collections
			.<Service> emptyList();

	private VOServicesController controller;
	private VORepository repository;
	private String search;
	private Class<? extends Capability> currentSearchType = ConeSearch.class;
	private List<Service> services = EMPTY_SERVICES;
	private VOServicesView view;
	private Service selectedService;

	public void clearServices() {
		setServices(EMPTY_SERVICES);
	}

	public VOServicesController getController() {
		if (controller == null) {
			controller = new VOServicesController();
			controller.setModel(this);
			controller.setView(getView());
		}
		return controller;
	}

	public Class<? extends Capability> getCurrentSearchType() {
		return currentSearchType;
	}

	public List<URI> getEndpoints() {
		return Arrays
				.asList(VORepository.DEFAULT_ENDPOINT,
						//URI.create("http://nvo.stsci.edu/vor10/ristandardservice.asmx"), 
						URI.create("http://registry.euro-vo.org/services/RegistrySearch"),
						URI.create("http://registry.astrogrid.org/astrogrid-registry/services/RegistryQueryv1_0"), 
						URI.create("http://alt.registry.astrogrid.org/astrogrid-registry/services/RegistryQueryv1_0"));
	}

	public VORepository getRepository() {
		if (repository == null) {
			repository = new VORepository();
		}
		return repository;
	}

	public String getSearch() {
		return search;
	}

	public List<Service> getServices() {
		return services;
	}

	public VOServicesView getView() {
		if (view == null) {
			view = new VOServicesView();
			view.setModel(this);
			view.setController(getController());
		}
		return view;
	}

	public List<Service> resourceSearch(Class<? extends Capability> searchType,
			String[] keywords) throws ErrorResp {
		return getRepository().resourceSearch(searchType, keywords);
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public void setCurrentSearchType(
			Class<? extends Capability> currentSearchType) {
		this.currentSearchType = currentSearchType;
	}

	public void setRepository(VORepository repo) {
		this.repository = repo;
	}

	public void setSearch(String search) {
		this.search = search;
		getView().setSearch(search);
	}

	public void setServices(List<Service> services) {
		getView().clearResults();
		this.services = services;
		getView().updateServices();
	}

	public void setView(VOServicesView view) {
		this.view = view;
	}

	public void setSelectedService(Service selectedService) {
		this.selectedService = selectedService;
		getView().updateSelection();
	}

	public Service getSelectedService() {
		return selectedService;
	}

	public URI getEndpoint() {
		return getRepository().getEndpoint();
	}

	public void setEndpoint(URI endpoint) {
		getRepository().setEndpoint(endpoint);
	}

	public Map<String, Boolean> parametersForSearchType(Class<? extends Capability> searchType) {

		if (searchType == null) {
			searchType = getCurrentSearchType();
		}
		
		Map<String, Boolean> parameters = new LinkedHashMap<String, Boolean>();
		if (searchType == ConeSearch.class) {
			// http://www.ivoa.net/Documents/latest/ConeSearch.html
			parameters.put("RA", true);
			parameters.put("DEC", true);
			parameters.put("SR", true);
			parameters.put("VERB", false);
			parameters.put("VERB", false);
		} else if (searchType == SimpleSpectralAccess.class) {
			// http://www.ivoa.net/Documents/SSA/20120210/index.html
			parameters.put("POS", true);
			parameters.put("SIZE", true);
			parameters.put("TIME", false);
			parameters.put("BAND", false);
			parameters.put("FORMAT", false);
			parameters.put("APERTURE", false);
			parameters.put("SPECRP", false);
			parameters.put("SPECRP", false);
			parameters.put("SPATRES", false);
			parameters.put("TIMERES", false);
			parameters.put("SNR", false);
			parameters.put("REDSHIFT", false);
			parameters.put("VARAMPL", false);
			parameters.put("TARGETNAME", false);
			parameters.put("TARGETCLASS", false);
			parameters.put("FLUXCALIB", false);
			parameters.put("WAVECALIB", false);
			parameters.put("PUBDID", false);
			parameters.put("CREATORDID", false);
			parameters.put("COLLECTION", false);
			parameters.put("TOP", false);
			parameters.put("MAXREC", false);
			parameters.put("MTIME", false);
			parameters.put("COMPRESS", false);
			parameters.put("RUNID", false);

		} else if (searchType == SimpleImageAccess.class) {
			// http://www.ivoa.net/Documents/SIA/20091116/
			parameters.put("POS", true);
			parameters.put("SIZE", true);
			
			parameters.put("INTERSECT", false);
			parameters.put("FORMAT", false);
			
			parameters.put("NAXIS", false);
			parameters.put("CFRAME", false);
			parameters.put("EQUINOX", false);
			parameters.put("CRPIX", false);
			parameters.put("CRVAL", false);
			parameters.put("CDELT", false);
			parameters.put("ROTANG", false);
			parameters.put("PROJ", false);
			
			
			
		} else if (searchType == SimpleLineAccess.class) {
			// http://www.ivoa.net/Documents/SLAP/20101209/index.html
			parameters.put("WAVELENGTH", true);
			parameters.put("REQUEST", false);
			parameters.put("VERSION", false);
			parameters.put("CHEMICAL_ELEMENT", false);
			parameters.put("INITIAL_LEVEL_ENERGY", false);
			parameters.put("FINAL_LEVEL_ENERGY", false);
			parameters.put("TEMPERATURE", false);
			parameters.put("EINSTEIN_A", false);
			parameters.put("PROCESS_TYPE", false);
			parameters.put("PROCESS_NAME", false);						
		} else {
			return parameters;
		}

		return parameters;
	}
}
