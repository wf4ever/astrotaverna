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

import net.ivoa.parameter.model.Expression;
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
 * PDLServicePArameter health checker
 * @author Julian Garrido
 * @since    19 May 2013
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
				System.out.println("----------------------------");
				System.out.println("SINK ACTIVITY: "+ activity.getClass().getName());
				logger.info("SINK ACTIVITY: "+ activity.getClass().getName());
				//System.err.println("processor: "+ p.getLocalName());
				//System.err.println("Dataflow: "+ d.getLocalName());
			
			for(Entry<String, SingleParameter> entry : paramMap.entrySet()){
				String name;
				SingleParameter param;
				ActivityInputPort aip;
				ProcessorInputPort pip;
				
				name = entry.getKey();
				param = entry.getValue();
				
				aip = Tools.getActivityInputPort((Activity<?>) activity, entry.getKey());
				
				if (aip == null) {
					//this is only in case some port names contain spaces in the pdl description.
					aip = Tools.getActivityInputPort((Activity<?>) activity, entry.getKey().replaceAll(" ", "_"));
					if (aip == null)
						continue;
				}
				pip = Tools.getProcessorInputPort(p, (Activity<?>) activity, aip);
				
				if (pip == null) { //if there is nothing connected to the port
					continue;
				}
				
				int count_valid_links = 0;
				for (Datalink dl : d.getLinks()) {					
					if (dl.getSink().equals(pip)) {
							//System.err.println("param: " + name + ". Link:" + dl.getSink().getName() + ". Source: "+ dl.getSource().getName() + " Source class: "+dl.getSource().getClass());
						Port source = dl.getSource();
						Set<VisitReport> subReports = checkSource(source, d, (Activity) activity, aip, param);
						for (VisitReport vr : subReports) {
						    vr.setProperty("activity", activity);
						    vr.setProperty("sinkPort", pip);
						}
						if(subReports!=null)
							reports.addAll(subReports);
						count_valid_links ++;
					}
				}
				if(count_valid_links > 1){
					System.err.println("It was expected only one link connecting to the sink input port. Activity: " + activity.getClass().getName() + "param: " + name);
					logger.error("It was expected only one link connecting to the sink input port. Activity: " + activity.getClass().getName() + "param: " + name);
				}
			}
		} catch (IOException e) {
			logger.error("Problem getting pdl descriptors for activity", e);
		} catch (NullPointerException e) {
			logger.error("Problem getting pdl descriptors for activity", e);
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
	
	//http://vo-param.googlecode.com/svn/trunk/model/documentation/PDL-Description_example01.xml
	//http://vo-param.googlecode.com/svn/trunk/model/documentation/PDL-Description_Example02.xml
	

	//InputPortTypeDescriptorActivity
	/**
	 * It checks if the metadata of source and sink ports are equal. During this comparation, string values
	 * are converted to lower case. 
	 * @param source
	 * @param d
	 * @param o
	 * @param aip
	 * @param sinkParam
	 * @return
	 */
	private Set<VisitReport> checkSource(Port source, Dataflow d, Activity o, ActivityInputPort aip, SingleParameter sinkParam) {
		Set<VisitReport> reports = new HashSet<VisitReport>();
		if (source instanceof ProcessorPort) {
			ProcessorPort processorPort = (ProcessorPort) source;
			Processor sourceProcessor = processorPort.getProcessor();
			Activity sourceActivity = sourceProcessor.getActivityList().get(0);
			System.out.println("++++++++++++++++++++++++++++++");
			System.out.println("(PDLParamChecker) SOURCE ACTIVITY:  "+ sourceActivity.getClass().getName());
			logger.info("SOURCE ACTIVITY:  "+ sourceActivity.getClass().getName());
			//if it is a PDLService (PDLServiceActivity implements InputPortSingleParameterActivity)
			//if (sourceActivity instanceof OutputPortSingleParameterActivity) {
			if (!(sourceActivity instanceof InputPortSingleParameterActivity)) {	
				
				VisitReport newReport = new VisitReport(PDLServiceParameterHealthCheck.getInstance(), o, "Source of " + aip.getName()+" Connected to a non PDL service", PDLServiceParameterHealthCheck.CONNECTED_TO_NON_PDL, Status.WARNING);
				newReport.setProperty("sinkPortName", aip.getName());
				newReport.setProperty("sourceName", sourceProcessor.getLocalName());
				newReport.setProperty("isProcessorSource", "true");
				reports.add(newReport);
				
				System.out.println("(PDLParamChecker)----not an inputportsingleParameter");
				logger.info("(PDLParamChecker)----not an inputportsingleParameter");
				
			}else{
			//	//System.err.println("\t La actividad de origen NO es una PDLServiceActivity");
			//	//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.DATATYPE_SOURCE, Status.WARNING);
			//	//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.NO_PROBLEM, Status.WARNING);
			//	VisitReport newReport = new VisitReport(PDLServiceParameterHealthCheck.getInstance(), o, "Source of " + aip.getName(), PDLServiceParameterHealthCheck.NO_ERROR, Status.WARNING);
			//	newReport.setProperty("sinkPortName", aip.getName());
			//	newReport.setProperty("sourceName", sourceProcessor.getLocalName());
			//	newReport.setProperty("isProcessorSource", "true");
			//	reports.add(newReport);		

				System.out.println("(PDLParamChecker)----An inputportsingleParameter, Source processor: "+ sourceProcessor.getLocalName()+", sourcename: "+ source.getName()+ ", sinkname: "+ aip.getName());
				logger.info("(PDLParamChecker)----An inputportsingleParameter, Source processor: "+ sourceProcessor.getLocalName()+", sourcename: "+ source.getName()+ ", sinkname: "+ aip.getName());
				VisitReport newReport;
				//System.err.println("\t La actividad de origen SI es tambien una PDLServiceActivity");
				Map<String, SingleParameter> paramSourceActivity;
				try {
					paramSourceActivity = ((OutputPortSingleParameterActivity) sourceActivity).getSingleParametersForOutputPorts();
				
					String sourcePortName = source.getName(); //this name has no white spaces because they are replaced.
					SingleParameter sourceParam = paramSourceActivity.get(sourcePortName);
					if(sourceParam==null){
						//paramSourceActivity may have names with white spaces.
						for(Entry<String, SingleParameter> entryParams : paramSourceActivity.entrySet()){
							if(entryParams.getKey().replaceAll(" ", "_").compareTo(sourcePortName)==0)
								sourceParam = paramSourceActivity.get(entryParams.getKey());
						}
						
					}
					String description = "";
					//compare with the SingleParameter from the inputPort
					if(sourceParam != null){
						int error = PDLServiceParameterHealthCheck.NO_ERROR;
						int countNonMetadataError = 0;
						int passedTests = 0;
						final int NUMBEROFERRORSTOCHECK = 5;
						if(sourceParam.getParameterType() == null && sinkParam.getParameterType() == null){
							error = error | PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
							countNonMetadataError++;
						}else if(sourceParam.getParameterType() == null || sinkParam.getParameterType() == null)
							error = error | PDLServiceParameterHealthCheck.TYPE_ERROR;
						if(sourceParam.getParameterType().value().toLowerCase().compareTo(sinkParam.getParameterType().value().toLowerCase())!=0){
							error = error | PDLServiceParameterHealthCheck.TYPE_ERROR;
						}else{
							passedTests ++;
						}
						//Expression exp = sourceParam.getPrecision(); //how to evaluate expressions???
						//error = error | PDLServiceParameterHealthCheck.PRECISION_ERROR;
						if(sourceParam.getUType() == null && sinkParam.getUType() == null ){
							error = error | PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
							countNonMetadataError++;
						}else if((sourceParam.getUType() == null || sinkParam.getUType() == null ))
							error = error | PDLServiceParameterHealthCheck.UTYPE_ERROR;
						else if((sourceParam.getUType().toLowerCase().compareTo(sinkParam.getUType().toLowerCase())!=0)){
							error = error | PDLServiceParameterHealthCheck.UTYPE_ERROR;
						}else
							passedTests ++;
						
						//if((sourceParam.getSkossConcept() == null && sinkParam.getSkossConcept() != null) 
						//		|| (sourceParam.getSkossConcept() != null &&sourceParam.getSkossConcept().compareTo(sinkParam.getSkossConcept())!=0)){
						//	error = error | PDLServiceParameterHealthCheck.SKOS_ERROR;
						//}
						if (sourceParam.getSkosConcept() == null && sinkParam.getSkosConcept() == null ){
							error = error | PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
							countNonMetadataError++;
						}else if((sourceParam.getSkosConcept() == null || sinkParam.getSkosConcept() == null ))
							error = error | PDLServiceParameterHealthCheck.SKOS_ERROR;
						else if((sourceParam.getSkosConcept().toLowerCase().compareTo(sinkParam.getSkosConcept().toLowerCase())!=0)){
							error = error | PDLServiceParameterHealthCheck.SKOS_ERROR;
						}else 
							passedTests ++;
						
						if(sourceParam.getUCD() == null &&  sinkParam.getUCD() == null){
							error = error | PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
							countNonMetadataError++;
						}else if(sourceParam.getUCD() == null ||  sinkParam.getUCD() == null)
							error = error | PDLServiceParameterHealthCheck.UCD_ERROR;
						else if(!areUCDsequals(sourceParam.getUCD().toLowerCase(), sinkParam.getUCD().toLowerCase())){
							error = error | PDLServiceParameterHealthCheck.UCD_ERROR;
						}else 
							passedTests ++;

						if(sourceParam.getUnit() == null && sinkParam.getUnit() == null ){
							error = error | PDLServiceParameterHealthCheck.NON_METADATA_ERROR;
							countNonMetadataError++;
						}else if((sourceParam.getUnit() == null || sinkParam.getUnit() == null ))
							error = error | PDLServiceParameterHealthCheck.UNIT_ERROR;
						else if((sourceParam.getUnit() != null && sinkParam.getUnit() != null ) 
								&& 	(sourceParam.getUnit().toLowerCase().compareTo(sinkParam.getUnit().toLowerCase())!=0)){
							error = error | PDLServiceParameterHealthCheck.UNIT_ERROR;
						} else
							passedTests ++;
						
						//Non metadata error i
						//if((countNonMetadataError + passedTests) == NUMBEROFERRORSTOCHECK)
						if(passedTests > 0 && error == PDLServiceParameterHealthCheck.NON_METADATA_ERROR)
							error = PDLServiceParameterHealthCheck.NO_ERROR;
						
						//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName()+ ". Metadata doesn't match", HealthCheck.NO_PROBLEM, Status.OK);
						if(error > 0 )
							newReport = new VisitReport(PDLServiceParameterHealthCheck.getInstance(), o, "Source of " + aip.getName()+ ". Metadata doesn't match", error, Status.WARNING);
						else
							newReport = new VisitReport(PDLServiceParameterHealthCheck.getInstance(), o, "Source of " + aip.getName()+ ".", error, Status.OK);
						
						newReport.setProperty("sinkPortName", aip.getName());
						newReport.setProperty("sourceName", sourceProcessor.getLocalName());
						newReport.setProperty("isProcessorSource", "true");
						reports.add(newReport);		
					}
				} catch (IOException e) {
					newReport = new VisitReport(PDLServiceParameterHealthCheck.getInstance(), o, "Source of " + aip.getName()+ ". An exception occurred",
								PDLServiceParameterHealthCheck.UNKNOWN, Status.WARNING);
					newReport.setProperty("sinkPortName", aip.getName());
					newReport.setProperty("sourceName", sourceProcessor.getLocalName());
					newReport.setProperty("isProcessorSource", "true");
					reports.add(newReport);		
				}
			}
		} else if (source instanceof MergeOutputPort) {
			Merge merge = ((MergePort) source).getMerge();
			for (MergeInputPort mip : merge.getInputPorts()) {
				for (Datalink dl : d.getLinks()) {
					if (dl.getSink().equals(mip)) {
						reports.addAll(checkSource(dl.getSource(), d, o, aip, sinkParam));
					}
				}
			}
		} else /* if (source instanceof DataflowInputPort) */  {
			//VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.DATATYPE_SOURCE, Status.WARNING);
			VisitReport newReport = new VisitReport(HealthCheck.getInstance(), o, "Source of " + aip.getName(), HealthCheck.INVALID_URL, Status.WARNING);
			newReport.setProperty("sinkPortName", aip.getName());
			newReport.setProperty("sourceName", source.getName());
			newReport.setProperty("isProcessorSource", "false");
			reports.add(newReport);
			
			
		} 
		return reports;
	}
	
	/**
	 * Compare to UCDs. It splits the UCDs by ';' so they might be in different order. 
	 * @param cad1
	 * @param cad2
	 * @return
	 */
	private boolean areUCDsequals(String cad1, String cad2){
		boolean equals = true;
		
		if(cad1==null && cad2 == null)
			return true;
		if(cad1 == null || cad2 == null)
			return false;
		
		String[] array1 = cad1.trim().split(";");
		String[] array2 = cad2.trim().split(";");
		
		if(array1.length == 0 && array2.length == 0)
			return true;
		
		equals = true;
		for(int i = 0; i<array1.length && equals; i++){
			boolean found = false;
			for(int j = 0; j<array2.length && !found; j++){
				if(array1[i].compareTo(array2[j])==0)
					found = true;
			}
			if(!found)
				equals = false;
		}
		
		return equals;
		
	}

}
