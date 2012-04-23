package uk.org.taverna.astro.vo;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.xml.namespace.QName;

import net.ivoa.xml.vodataservice.v1.ParamHTTP;
import net.ivoa.xml.voresource.v1.AccessURL;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Interface;
import net.ivoa.xml.voresource.v1.Service;
import net.ivoa.xml.voresource.v1.WebService;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;
import net.sf.taverna.t2.workbench.ui.workflowview.WorkflowView;

import org.apache.log4j.Logger;

public class VOServicesController {

	private static Logger logger = Logger.getLogger(VOServicesController.class);

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
			if (currentSearchTask == this) {
				currentSearchTask = null;
			}
		}
	}

	// Current state
	private SearchTask currentSearchTask;

	private VOServicesModel model;

	private VOServicesComponent view;

	public VOServicesController() {
	}

	public void addToWorkflow(Service service) {

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

	protected void cancelSearchTaskIfNeeded() {
		if (currentSearchTask != null) {
			currentSearchTask.cancel(true);
			currentSearchTask = null;
		}
	}

	public VOServicesModel getModel() {
		if (model == null) {
			model = new VOServicesModel();
		}
		return model;
	}

	public VOServicesComponent getView() {
		if (view == null) {
			view = new VOServicesComponent();
			view.setController(this);
			view.setModel(getModel());
		}
		return view;
	}

	public void search(Class<? extends Capability> searchType, String search) {
		cancelSearchTaskIfNeeded();
		getView().statusSearching(searchType, search);
		currentSearchTask = new SearchTask(searchType, search);
		getModel().setSelectedService(null);
		getModel().clearServices();
		currentSearchTask.execute();
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

	public void setView(VOServicesComponent view) {
		this.view = view;
	}

	public void selectService(Service service) {
		getModel().setSelectedService(service);
	}

}
