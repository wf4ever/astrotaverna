package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Service;
import uk.org.taverna.astro.vorepo.VORepository;
import uk.org.taverna.astro.wsdl.registrysearch.ErrorResp;

public class VOServicesModel {
	private static List<Service> EMPTY_SERVICES = Collections.<Service> emptyList();

	private VOServicesController controller;
	private VORepository repository;
	private String search;
	private Class<? extends Capability> searchType = ConeSearch.class;
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
		return searchType;
	}

	public List<URI> getRegistries() {
		return Arrays.asList(VORepository.DEFAULT_ENDPOINT);
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
		this.searchType = currentSearchType;
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

}
