package org.purl.wf4ever.astrotaverna.pdl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivityConfigurationBean;

/**
 * Stilts health checker
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class PDLServiceHealthChecker implements
		HealthChecker<PDLServiceActivity> {

	public boolean canVisit(Object o) {
		// Return True if we can visit the object. We could do
		// deeper (but not time consuming) checks here, for instance
		// if the health checker only deals with StiltsActivity where
		// a certain configuration option is enabled.
		return o instanceof PDLServiceActivity;
	}

	public boolean isTimeConsuming() {
		// Return true if the health checker does a network lookup
		// or similar time consuming checks, in which case
		// it would only be performed when using File->Validate workflow
		// or File->Run.
		return false;
	}

	public VisitReport visit(PDLServiceActivity activity, List<Object> ancestry) {
		PDLServiceActivityConfigurationBean config = activity.getConfiguration();

		// We'll build a list of subreports
		List<VisitReport> subReports = new ArrayList<VisitReport>();

		/*
		if (!config.getExampleUri().isAbsolute()) {
			// Report Severe problems we know won't work
			VisitReport report = new VisitReport(HealthCheck.getInstance(),
					activity, "Example URI must be absolute", HealthCheck.INVALID_URL,
					Status.SEVERE);
			subReports.add(report);
		}

		if (config.getExampleString().equals("")) {
			// Warning on possible problems
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"Example string empty", HealthCheck.NO_CONFIGURATION,
					Status.WARNING));
		}
		*/
		File file = new File(config.getPdlDescriptionFile());
		if(!file.exists()){
			try {
				URI exampleUri = new URI(config.getPdlDescriptionFile());
			} catch (URISyntaxException e) {
				subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
						"File does not exist or invalid URI.", HealthCheck.INVALID_CONFIGURATION,
						Status.SEVERE));
			}
		}
		
		if( !(config.getServiceType().compareTo(config.PDLSERVICE) == 0 
				|| config.getServiceType().compareTo(config.RESTSERVICE) == 0
				|| config.getServiceType().compareTo(config.VOTABLERESTSERVICE) == 0)){
			
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"Invalid service type.", HealthCheck.INVALID_CONFIGURATION,
					Status.SEVERE));
		}

		// The default explanation here will be used if the subreports list is
		// empty
		return new VisitReport(HealthCheck.getInstance(), activity,
				"Validation service OK", HealthCheck.NO_PROBLEM, subReports);
	}

}
