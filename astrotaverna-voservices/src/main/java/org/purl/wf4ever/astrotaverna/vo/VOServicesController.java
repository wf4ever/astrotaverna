package org.purl.wf4ever.astrotaverna.vo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.WebService;
import net.sf.taverna.t2.activities.rest.URISignatureHandler;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;

import org.apache.log4j.Logger;
import org.purl.wf4ever.astrotaverna.vorepo.VORepository.Status;


public class VOServicesController {

	public class CheckStatus extends SwingWorker<Status, String> {
		private final URI oldEndpoint;

		public CheckStatus(URI oldEndpoint) {
			this.oldEndpoint = oldEndpoint;
		}

		@Override
		protected Status doInBackground() throws Exception {
			return getModel().getRepository().getStatus();
		}

		@Override
		protected void done() {
			try {
				Status status;
				try {
					status = get();
				} catch (CancellationException e) {
					return;
				} catch (InterruptedException e) {
					getView().statusEndpointStatus(Status.ERROR);
					return;
				} catch (ExecutionException e) {
					getView().statusEndpointStatus(Status.ERROR);
					revert();
					return;
				}
				if (status != Status.OK) {
					getView().statusEndpointStatus(status);
					revert();
					return;
				}

				getView().statusEndpointOK();
				// TODO: Store in preferences?
				// getModel().addEndpoint(endpoint);
			} finally {
				if (currentTask == this) {
					currentTask = null;
				}
			}
		}

		private void revert() {
			if (oldEndpoint != null) {
				changeEndpoint(oldEndpoint);
			}
		}

	}

	public class SearchTask extends SwingWorker<List<Service>, String> {
		private String search;
		private final Class<? extends Capability> searchType;

		public SearchTask(Class<? extends Capability> searchType, String search) {
			this.searchType = searchType;
			this.search = search;
		}

		@Override
		protected List<Service> doInBackground() throws Exception {
			return getModel().resourceSearch(searchType, search.split(" "));
		}

		@Override
		protected void done() {
			try {
				List<Service> resources;
				try {
					resources = get();
				} catch (CancellationException ex) {
					logger.info("Cancelled search", ex);
					getView().statusCancelled(ex);
					return;
				} catch (InterruptedException ex) {
					logger.warn("Interrupted search", ex);
					getView().statusInterrupted(ex);
					return;
				} catch (ExecutionException ex) {
					logger.warn("Failed search", ex);
					getView().statusFailed(ex);
					return;
				}
				getModel().setSearch(search);
				getModel().setCurrentSearchType(searchType);
				getView().statusFoundResults(resources.size());
				getModel().setServices(resources);
			} finally {
				if (currentTask == this) {
					currentTask = null;
				}
			}
		}
	}

	private static Logger logger = Logger.getLogger(VOServicesController.class);

	// Current state
	private SwingWorker<?, ?> currentTask;

	private VOServicesModel model;

	private VOServicesView view;

	public VOServicesController() {
	}

	public void addToWorkflow() {
		Service service = getModel().getSelectedService();
		VOServiceDescription serviceDescription = makeServiceDescription(
				service, getModel().getCurrentSearchType());
		if (serviceDescription.getUrlSignature() == null) {
			String message = "No REST (ParamHTTP) interface found for service "
					+ service.getShortName();
			logger.warn(message);
			JOptionPane.showMessageDialog(getView(), message,
					"Could not add to workflow", JOptionPane.WARNING_MESSAGE);
			return;
		}
	

		AddToWorkflowDialog addDialog = new AddToWorkflowDialog(
				serviceDescription, service);
		addDialog.setController(this);
		addDialog.setModel(getModel());
		addDialog.setLocationRelativeTo(getView());
		addDialog.setVisible(true);

	}

	public VOServiceDescription makeServiceDescription(Service service,
			Class<? extends Capability> searchType) {
		VOServiceDescription serviceDescription = new VOServiceDescription();
		for (Capability c : service.getCapability()) {			
			if (searchType != null && !(searchType.isInstance(c))) {
				continue;
			}
			for (Interface i : c.getInterface()) {
				if (i instanceof ParamHTTP) {
					serviceDescription.setIdentifier(URI.create(service
							.getIdentifier().trim()));
					String searchTypeName = searchType.getSimpleName();
					serviceDescription.setSearchType(searchTypeName);
					
					String serviceName = service.getShortName();
					if (serviceName == null || serviceName.trim().isEmpty()) {
						serviceDescription.setName(searchTypeName);
					} else {
						serviceDescription.setName(serviceName.trim());						
					}

					ParamHTTP http = (ParamHTTP) i;
					AccessURL accessURL = http.getAccessURL().get(0);
					serviceDescription
							.setAccessURL(accessURL.getValue().trim());
					updateAccessUrl(serviceDescription, searchType);
					break;
				}
				if (i instanceof WebService) {
					// TODO: Potentially loads to do here
				}
			}
		}
		return serviceDescription;
	}

	public void addToWorkflow(VOServiceDescription serviceDescription) {
		updateAccessUrl(serviceDescription, getModel().getCurrentSearchType());

		WorkflowView.importServiceDescription(serviceDescription, false);
		Workbench.getInstance().getPerspectives().setWorkflowPerspective();
		// TODO: Make and connect string constants
	}

	protected void updateAccessUrl(VOServiceDescription serviceDescription, Class<? extends Capability> searchType) {
		String urlSignature = serviceDescription.getAccessURL();
		if (!urlSignature.contains("?")) {
			urlSignature += "?";
		} else if (!urlSignature.endsWith("&")) {
			urlSignature += "&";
		}
		Map<String, String> values = serviceDescription.getParameterValues();
		Map<String, Boolean> parameters = getModel().parametersForSearchType(searchType);
		for (Entry<String, Boolean> parameterIsRequired : parameters.entrySet()) {
			String param = parameterIsRequired.getKey();
			String value = values.get(param);
			if (value != null && !value.isEmpty()) {
				// TODO: Do parameters as string constants instead
				urlSignature += String.format("%s=%s&", param,
						escapeURIParameter(value));
			} else if (parameterIsRequired.getValue() || value != null) {
				// non-null but empty means include as parameter
				urlSignature += String.format("%s={%s}&", param, param);
			}
		}
		// Trim trailing &
		if (urlSignature.endsWith("&")) {
			urlSignature = urlSignature.substring(0, urlSignature.length() - 1);
		}
		serviceDescription.setUrlSignature(urlSignature);
	}

	public static String escapeURIParameter(String string) {
		return URISignatureHandler.urlEncodeQuery(string);
	}

	protected void cancelTaskIfNeeded() {
		if (currentTask != null) {
			currentTask.cancel(true);
			currentTask = null;
		}
	}

	public void changeEndpoint(String uri) {
		URI oldEndpoint = getModel().getEndpoint();
		URI endpoint;
		try {
			endpoint = new URI(uri);
		} catch (URISyntaxException e) {
			getView().statusInvalidEndpoint(e);
			changeEndpoint(oldEndpoint);
			return;
		}
		changeEndpoint(endpoint);
		checkEndpoint(oldEndpoint);
	}

	protected void changeEndpoint(URI endpoint) {
		getModel().setEndpoint(endpoint);
		getView().updateEndpoint();
	}

	public void checkEndpoint() {
		checkEndpoint(null);
	}

	protected void checkEndpoint(URI revertToEndpoint) {
		cancelTaskIfNeeded();
		getView().statusEndpointChecking();
		currentTask = new CheckStatus(revertToEndpoint);
		currentTask.execute();
	}

	public VOServicesModel getModel() {
		if (model == null) {
			model = new VOServicesModel();
		}
		return model;
	}

	public VOServicesView getView() {
		if (view == null) {
			view = new VOServicesView();
			view.setController(this);
			view.setModel(getModel());
		}
		return view;
	}

	public void search(Class<? extends Capability> searchType, String search) {
		cancelTaskIfNeeded();
		getView().statusSearching(searchType, search);
		currentTask = new SearchTask(searchType, search);
		getModel().setSelectedService(null);
		getModel().clearServices();
		currentTask.execute();
	}

	public void selectService(Service service) {
		getModel().setSelectedService(service);
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	public void setView(VOServicesView view) {
		this.view = view;
	}

}
