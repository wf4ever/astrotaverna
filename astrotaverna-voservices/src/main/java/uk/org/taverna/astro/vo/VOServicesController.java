package uk.org.taverna.astro.vo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.WebService;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;

import org.apache.log4j.Logger;

import uk.org.taverna.astro.vorepo.VORepository.Status;

public class VOServicesController {

	private static Logger logger = Logger.getLogger(VOServicesController.class);

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

	// Current state
	private SwingWorker<?, ?> currentTask;

	private VOServicesModel model;

	private VOServicesView view;

	public VOServicesController() {
	}

	public void addToWorkflow() {
		Service service = getModel().getSelectedService();
		VOServiceDescription restServiceDescription = new VOServiceDescription();
		for (Capability c : service.getCapability()) {
			if (!(getModel().getCurrentSearchType().isInstance(c))) {
				continue;
			}
			for (Interface i : c.getInterface()) {
				if (i instanceof ParamHTTP) {
					ParamHTTP http = (ParamHTTP) i;
					AccessURL accessURL = http.getAccessURL().get(0);
					restServiceDescription.setAccessURL(accessURL.getValue()
							.trim());
					restServiceDescription.setIdentifier(URI.create(service
							.getIdentifier().trim()));
					restServiceDescription.setName(service.getShortName());
					restServiceDescription.setSearchType(getModel()
							.getCurrentSearchType().getSimpleName());
					break;
				}
				if (i instanceof WebService) {
					// TODO: Potentially loads to do here
				}
			}
		}
		if (restServiceDescription.getAccessURL() == null) {
			// TODO: Show error

			return;
		}

		WorkflowView.importServiceDescription(restServiceDescription, false);
		Workbench.getInstance().getPerspectives().setWorkflowPerspective();

	}

	protected void cancelTaskIfNeeded() {
		if (currentTask != null) {
			currentTask.cancel(true);
			currentTask = null;
		}
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

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	public void setView(VOServicesView view) {
		this.view = view;
	}

	public void selectService(Service service) {
		getModel().setSelectedService(service);
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

	protected void checkEndpoint(URI revertToEndpoint) {
		cancelTaskIfNeeded();
		getView().statusEndpointChecking();
		currentTask = new CheckStatus(revertToEndpoint);
		currentTask.execute();
	}

	protected void changeEndpoint(URI endpoint) {
		getModel().setEndpoint(endpoint);
		getView().updateEndpoint();
	}

	public void checkEndpoint() {
		checkEndpoint(null);
	}
}
