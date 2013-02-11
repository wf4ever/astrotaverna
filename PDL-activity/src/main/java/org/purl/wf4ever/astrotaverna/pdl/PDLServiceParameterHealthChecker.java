package org.purl.wf4ever.astrotaverna.pdl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import net.ivoa.parameter.model.SingleParameter;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Merge;
import net.sf.taverna.t2.workflowmodel.MergeInputPort;
import net.sf.taverna.t2.workflowmodel.MergeOutputPort;
import net.sf.taverna.t2.workflowmodel.MergePort;
import net.sf.taverna.t2.workflowmodel.Port;
import net.sf.taverna.t2.workflowmodel.Processor;
import net.sf.taverna.t2.workflowmodel.ProcessorInputPort;
import net.sf.taverna.t2.workflowmodel.Datalink;
import net.sf.taverna.t2.workflowmodel.ProcessorPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.health.HealthChecker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

/**
 * Stilts health checker
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class PDLServiceParameterHealthChecker implements
		HealthChecker<InputPortSingleParameterActivity> {

	private static Logger logger = Logger.getLogger(PDLServiceParameterHealthChecker.class);
	
	public boolean canVisit(Object obj) {
		// Return True if we can visit the object. We could do
		// deeper (but not time consuming) checks here, for instance
		// if the health checker only deals with StiltsActivity where
		// a certain configuration option is enabled.
		return obj instanceof InputPortSingleParameterActivity;
	}

	public boolean isTimeConsuming() {
		// Return true if the health checker does a network lookup
		// or similar time consuming checks, in which case
		// it would only be performed when using File->Validate workflow
		// or File->Run.
		return false;
	}

	@Override
	public VisitReport visit(InputPortSingleParameterActivity activity, List<Object> ancestry) {
		
		//PDLServiceActivityConfigurationBean config = activity.getConfiguration();

		// We'll build a list of subreports
		List<VisitReport> reports = new ArrayList<VisitReport>();

		try{
			Map<String, SingleParameter> paramMap = activity.getSingleParametersForInputPorts();
			Processor p = (Processor) VisitReport.findAncestor(ancestry, Processor.class);
			Dataflow d = (Dataflow) VisitReport.findAncestor(ancestry, Dataflow.class);
					
			for(Entry<String, SingleParameter> entry : paramMap.entrySet()){
				String name;
				SingleParameter param;
				ActivityInputPort aip;
				ProcessorInputPort pip;
				
				name = entry.getKey();
				param = entry.getValue();
				
				aip = Tools.getActivityInputPort((Activity<?>) activity, entry.getKey());
				if (aip == null) {
					continue;
				}
				pip = Tools.getProcessorInputPort(p, (Activity<?>) activity, aip);
				
				if (pip == null) {
					continue;
				}
				for (Datalink dl : d.getLinks()) {
	
					if (dl.getSink().equals(pip)) {
						Port source = dl.getSource();
						Set<VisitReport> subReports = checkSource(source, d, (Activity) activity, aip);
						for (VisitReport vr : subReports) {
						    vr.setProperty("activity", activity);
						    vr.setProperty("sinkPort", pip);
						}
						reports.addAll(subReports);
					}
				}
			}
		} catch (IOException e) {
			logger.error("Problem getting type descriptors for activity", e);
		} catch (NullPointerException e) {
			logger.error("Problem getting type desciptors for activity", e);
		}
		if (reports.isEmpty()) {
			return null;
		}
		if (reports.size() == 1) {
			return reports.get(0);
		}
		else {
			return new VisitReport(HealthCheck.getInstance(), activity, "Collation", HealthCheck.DEFAULT_VALUE, reports);
		}
		

	}

	//InputPortTypeDescriptorActivity
	private Set<VisitReport> checkSource(Port source, Dataflow d, Activity o, ActivityInputPort aip) {
		Set<VisitReport> reports = new HashSet<VisitReport>();
		if (source instanceof ProcessorPort) {
			ProcessorPort processorPort = (ProcessorPort) source;
			Processor sourceProcessor = processorPort.getProcessor();
			Activity sourceActivity = sourceProcessor.getActivityList().get(0);
			if (!(sourceActivity instanceof InputPortSingleParameterActivity)) {
				//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.DATATYPE_SOURCE, Status.WARNING);
				VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.NO_PROBLEM, Status.WARNING);
				newReport.setProperty("sinkPortName", aip.getName());
				newReport.setProperty("sourceName", sourceProcessor.getLocalName());
				newReport.setProperty("isProcessorSource", "true");
				reports.add(newReport);
			}
		} else if (source instanceof MergeOutputPort) {
			Merge merge = ((MergePort) source).getMerge();
			for (MergeInputPort mip : merge.getInputPorts()) {
				for (Datalink dl : d.getLinks()) {
					if (dl.getSink().equals(mip)) {
						reports.addAll(checkSource(dl.getSource(), d, o, aip));
					}
				}
				
			}
		} else /* if (source instanceof DataflowInputPort) */  {
			//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.DATATYPE_SOURCE, Status.WARNING);
			VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.UNKNOWN_OPERATION, Status.WARNING);
			newReport.setProperty("sinkPortName", aip.getName());
			newReport.setProperty("sourceName", source.getName());
			newReport.setProperty("isProcessorSource", "false");
			reports.add(newReport);
		} 
		return reports;
	}

}
