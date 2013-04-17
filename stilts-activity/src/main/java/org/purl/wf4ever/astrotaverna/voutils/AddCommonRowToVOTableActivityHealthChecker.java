package org.purl.wf4ever.astrotaverna.voutils;

import java.util.ArrayList;
import java.util.List;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

/**
 * Add common row to votable health checker
 * @author Julian Garrido
 * @since    17 April 2013
 */
public class AddCommonRowToVOTableActivityHealthChecker implements
		HealthChecker<AddCommonRowToVOTableActivity> {

	public boolean canVisit(Object o) {
		// Return True if we can visit the object. We could do
		// deeper (but not time consuming) checks here, for instance
		// if the health checker only deals with StiltsActivity where
		// a certain configuration option is enabled.
		return o instanceof AddCommonRowToVOTableActivity;
	}

	public boolean isTimeConsuming() {
		// Return true if the health checker does a network lookup
		// or similar time consuming checks, in which case
		// it would only be performed when using File->Validate workflow
		// or File->Run.
		return false;
	}

	public VisitReport visit(AddCommonRowToVOTableActivity activity, List<Object> ancestry) {
		AddCommonRowToVOTableActivityConfigurationBean config = activity.getConfiguration();

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
		/*
		if(!(      config.getTypeOfFilter().compareTo("Column names")==0 
				|| config.getTypeOfFilter().compareTo("UCDs")==0)){
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"Invalid filter type.", HealthCheck.INVALID_CONFIGURATION,
					Status.WARNING));
		}*/
		
		if(!(      config.getTypeOfInput().compareTo("File")==0
				|| config.getTypeOfInput().compareTo("URL")==0
				|| config.getTypeOfInput().compareTo("String")==0)){
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"Invalid input type.", HealthCheck.INVALID_CONFIGURATION,
					Status.WARNING));
		}
		if(!(config.getCommonRowPosition().compareTo("Left")==0
				|| config.getCommonRowPosition().compareTo("Right")==0)){
			subReports.add(new VisitReport(HealthCheck.getInstance(), activity,
					"Invalid common row position.", HealthCheck.INVALID_CONFIGURATION,
					Status.WARNING));
		}
		
		// The default explanation here will be used if the subreports list is
		// empty
		return new VisitReport(HealthCheck.getInstance(), activity,
				"Stilts service OK", HealthCheck.NO_PROBLEM, subReports);
	}

}
