package org.purl.wf4ever.astrotaverna.pdl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
//comment from terminal
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;


import CommonsObjects.GeneralParameter;

//import uk.ac.starlink.ttools.Stilts;
import visitors.GeneralParameterVisitor;

import net.ivoa.parameter.model.ConditionalStatement;
import net.ivoa.parameter.model.ConstraintOnGroup;
import net.ivoa.parameter.model.ParameterGroup;
import net.ivoa.parameter.model.ParameterReference;
import net.ivoa.parameter.model.Service;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.expression.ExpressionParserFactory;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupHandlerHelper;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupProcessor;
import net.ivoa.pdl.interpreter.utilities.UserMapper;
import net.ivoa.pdl.interpreter.utilities.Utilities;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class PDLServiceActivity extends
		AbstractAsynchronousActivity<PDLServiceActivityConfigurationBean>
		implements AsynchronousActivity<PDLServiceActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private static Logger logger = Logger.getLogger(PDLServiceActivity.class);
	
	//private static final String OUT_SIMPLE_OUTPUT = "outputFileOut";
	private static final String OUT_REPORT = "status";
	private static final String RESPONSE_BODY = "response_body";
	private static final String DEFAULT_OUTPUT = "fileResult";
	
	private PDLServiceActivityConfigurationBean configBean;
	
	
	private HashMap<String, SingleParameter> hashAllParameters;
	private HashMap<String, String> restrictionsOnGroups;
	private String serviceDescription;
	
	//pdl specific objects
//	final public String complete = "To complete";
//	final public String error = "With error";
//	final public String valid = "Valid";

	@Override
	public void configure(PDLServiceActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks

		
		//this method controls if the input is valid
		
		//service.getParameters().getParameter();
		
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// myClient = new MyClient(configBean.getExampleUri());
		// this.service = myClient.getService(configBean.getExampleString());

		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() throws ActivityConfigurationException {
		//GroupProcessor gp;
//		Service service;
		//ArrayList<List<SingleParameter>> paramsLists;
		//HashMap<String, Integer> dimensions;
		
		PDLServiceController pdlcontroller;
		
		removeInputs();
		removeOutputs();
		
		try{
			pdlcontroller = new PDLServiceController (this.configBean);
			pdlcontroller.prepareHashParametersInputs();
			hashAllParameters = pdlcontroller.getHashAllParameters();
			pdlcontroller.prepareRestrictions();
			restrictionsOnGroups = pdlcontroller.getRestrictionsOnGroups();
			serviceDescription = pdlcontroller.getServiceDescription();
			
//			service = buildService(configBean.getPdlDescriptionFile());
//			Utilities.getInstance().setService(service);
//			Utilities.getInstance().setMapper(new UserMapper());
			// In case we are being reconfigured - remove existing ports first
			// to avoid duplicates
			
	
			//service.getInputs().getConstraintOnGroup().getConditionalStatement().
//			List<SingleParameter> serviceParameters = service.getParameters().getParameter();
			
//			List<ParameterReference> inputParamRefs = getParameterRefeferences(service.getInputs());
//			List<ParameterReference> outputParamRefs = getParameterRefeferences(service.getOutputs());
			
//			ArrayList<SingleParameter> inputParameters = getSubsetOfSingleParameter(serviceParameters, inputParamRefs);
//			ArrayList<SingleParameter> outputParameters = getSubsetOfSingleParameter(serviceParameters, outputParamRefs);
			
//			//Input ports
//			hashParameters = new HashMap();
			
			//Input ports
			HashMap<String, SingleParameter> inputSingleParams = pdlcontroller.getHashInputParameters();
			for(String paramName : inputSingleParams.keySet()){
				addInput(paramName, 0, true, null, String.class);
			}
			
			//Output ports
			HashMap<String, SingleParameter> outputSingleParams = pdlcontroller.getHashOutputParameters();
			for(String paramName : outputSingleParams.keySet()){
				addOutput(paramName, 0);
			}
			if(outputSingleParams==null || outputSingleParams.isEmpty())
				addOutput(DEFAULT_OUTPUT,0);
			addOutput(OUT_REPORT, 0);
			addOutput(RESPONSE_BODY, 0); 
			
			/*
//			for(SingleParameter param: inputParameters){
//				addInput(param.getName(), 0, true, null, String.class);
//				hashParameters.put(param.getName(), param);
//			}
			
//			//Output ports
//			for(SingleParameter param: outputParameters){
//				// Single value output port (depth 0)
//				addOutput(param.getName(), 0);
//				hashParameters.put(param.getName(), param);
//			}
	
			
			//This port is for testing
			// Single value output port (depth 0)
			
			
			
			
			//restrictions 
//			 HashMap<String, String> inputRestrictions = getRestrictionsOnGroup(service.getInputs());
//			 HashMap<String, String> outputRestrictions = getRestrictionsOnGroup(service.getOutputs());
//			 restrictionsOnGroups = new HashMap<String,String>();
//			 if(inputRestrictions!=null){
//				 restrictionsOnGroups.putAll(inputRestrictions);
//			 }
//			 if(outputRestrictions!=null){
//				 restrictionsOnGroups.putAll(outputRestrictions);
//			 }
//			 
//			 serviceDescription = service.getDescription();
			
			// Replace with your input and output port definitions
			
			//The following commented code is a not efficient way to extract the inputParameters
			gp = new GroupProcessor(service);
			//System.out.println(service.getInputs().getParameterRef().get(0).getParameterName());
			gp.process();
			List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
			//paramsLists = new ArrayList();
			//dimensions = new HashMap();
			hashParameters = new HashMap();
			for(GroupHandlerHelper ghh : groupsHandler){
				List<SingleParameter> paramsList = ghh.getSingleParamIntoThisGroup();
				for(SingleParameter param: paramsList){
					//The following code is commented to ignore the dimension value, due to 
					//taverna has a native way to handle grids/arrays/lists
					//int dimension = -1;
					//if(param.getDimension()!=null){
					//	try{
					//		String value = ExpressionParserFactory.getInstance()
					//		   .buildParser(param.getDimension()).parse().get(0).getValue();
					//		dimension = new Integer(value).intValue();
					//	} catch (Exception ex){
					//		logger.error("I couln't read the dimension value for "+ param.getName());
					//		dimension = -1;
					//	}
					//}
					//if(dimension > 1 ){
					//	addInput(param.getName(), 1, true, null, String.class);	
					//	//dimensions.put(param.getName(), new Integer(1));
					//}else{
					addInput(param.getName(), 0, true, null, String.class);
					//	//dimensions.put(param.getName(), new Integer(0));
					//}
					hashParameters.put(param.getName(), param);
				}
				//if(paramsList!=null && paramsLists.size()>0)
				//	paramsLists.add(paramsList);
					
			}
			*/
		}catch(ActivityConfigurationException ex){
			logger.warn("unexisting or invalid pdl description file: the service will not have inports");
		}

		// Single value output port (depth 0)
		//addOutput(OUT_SIMPLE_OUTPUT, 0);
		// Single value output port (depth 0)
		//addOutput(OUT_REPORT, 0);

	}
	
	
//	public String getServiceDescription() {
//		return serviceDescription;
//	}


	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		
		final PDLServiceActivityConfigurationBean config = this.configBean;
		
		callback.requestRun(new Runnable() {
		
//			GroupProcessor gp;
//			Service service;
			
			PDLServiceController pdlcontroller;
			
			//ArrayList<List<SingleParameter>> paramsLists;
			//HashMap<String, Integer> dimensions;
			
			/*
			 * Check if the mandatory inputs are not null
			 */
			public boolean areMandatoryInputsNotNull(){                  
				boolean validStatus = true;
            
				try{
//					List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
//					for(GroupHandlerHelper ghh : groupsHandler){
//						List<SingleParameter> paramsList = ghh.getSingleParamIntoThisGroup();
					List<SingleParameter> paramsList = pdlcontroller.getSingleParametersOnGroups();
					for(SingleParameter param: paramsList){
						if(inputs.get(param.getName())==null)
							//if no dependency defined --> false
							//if no optional --> false
							if(!(param.getDependency()!=null && param.getDependency().compareTo("optional")==0))
								validStatus = false; 
						
					}
//					}
				}catch(Exception ex){validStatus = false;}
				return validStatus;
			}
			
			/*
			 * This method will fail because output parameters have no value before the service is invoked.
			 */
			private void checkInfo(){
				List<SingleParameter> paramList = Utilities.getInstance().getService()
						.getParameters().getParameter();
				
				for (int i = 0; i < paramList.size(); i++) {
					SingleParameter p = paramList.get(i);
					//System.out.println(p.getName());
					List<GeneralParameter> genparlist = Utilities.getInstance().getuserProvidedValuesForParameter(p);
					if(genparlist != null && genparlist.size()!=0){
						String value =Utilities.getInstance().getuserProvidedValuesForParameter(p).get(0).getValue();
						System.out.println(p.getName()+", "+ value);
					}else{
						System.out.println(p.getName()+", no value" );
					}
				}
			}
			
			
			
			
			/*
			public void run4TestNullInputs() {
				boolean callbackfails=false;
				
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
			
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				String message ="null inputs: ";
				
				try{
					service = buildService(configBean.getPdlDescriptionFile());

					Utilities.getInstance().setService(service);
					Utilities.getInstance().setMapper(new UserMapper());
					
					gp = new GroupProcessor(service);
					gp.process();
				}catch (ActivityConfigurationException e) {
					callback.fail("Make sure that the service configuration has an url that points to a valid pdl description file");
					callbackfails = true;
				}
				
				List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
				for(GroupHandlerHelper ghh : groupsHandler){
					List<SingleParameter> paramsList = ghh.getSingleParamIntoThisGroup();
					for(SingleParameter param: paramsList){
						if(inputs.get(param.getName())==null)
							message += param.getName()+", ";
					}
				}
				
				T2Reference simpleRef2 = referenceService.register(message,0, true, context); 
				outputs.put(OUT_REPORT, simpleRef2);
				callback.receiveResult(outputs, new int[0]);
			}
			*/
			
			/**
			 * It processes the inputs from the taverna activity and returns a hashMap with the pairs <name, value>
			 * It only takes the inputs that are not null. It doesn't check if the inputs are or aren't mandatory
			 * @param inputs
			 * @param context
			 * @param referenceService
			 * @return
			 */
			private HashMap getInputsMap(final Map<String, T2Reference> inputs, InvocationContext context, ReferenceService referenceService) throws InvalidParameterException{
				HashMap resultMap = new HashMap();
				if(pdlcontroller!=null){
					
					HashMap<String, SingleParameter> inputSingleParameterMap = pdlcontroller.getHashInputParameters();
					
					for(Map.Entry<String, SingleParameter> entry: inputSingleParameterMap.entrySet()){
						if(inputs.get(entry.getValue().getName()) != null){
							int dimension = PDLServiceController.getDimension(entry.getValue());
							if(dimension==1){
								String value = (String) referenceService.renderIdentifier(inputs.get(entry.getValue().getName()), 
										String.class, context);
								resultMap.put(entry.getValue().getName(), value);
							}else{
								List<String> values = (List<String>) referenceService.renderIdentifier(inputs.get(entry.getValue().getName()), 
										String.class, context);
								resultMap.put(entry.getValue().getName(), values);
							}
						}
					}
				}
				return resultMap;
			}
			
			public void run() {
				boolean callbackfails=false;
				String serviceResult; 
				String jobInfo = "";
				String jobId;
				String userId;
				boolean inputsAreValid = false;
							
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
				
				GroupProcessor gp;
				
				
				serviceResult = 
						"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> " +
						"<JobsList> " +
						"    <ServiceName>http://pdl-calc.obspm.fr:8081/montage/</ServiceName> " +
						"    <List> " +
						"        <JobId>3</JobId> " +
						"        <UserId>7</UserId> " +
						"    </List> " +
						"</JobsList>";
				
				try {
					try{
						pdlcontroller = new PDLServiceController (configBean);
						pdlcontroller.prepareHashParametersInputs();
						hashAllParameters = pdlcontroller.getHashAllParameters();
						pdlcontroller.prepareRestrictions();
						restrictionsOnGroups = pdlcontroller.getRestrictionsOnGroups();
						pdlcontroller.prepareProcess();
						serviceDescription = pdlcontroller.getServiceDescription();
//						service = buildService(configBean.getPdlDescriptionFile());
//
//						Utilities.getInstance().setService(service);
//						Utilities.getInstance().setMapper(new UserMapper());
//						
//						gp = new GroupProcessor(service);
//						gp.process();
					}catch (ActivityConfigurationException e) {
						callback.fail("Make sure that the service configuration has an url that points to a valid pdl description file"+"\n"+e.getMessage());
						logger.error("Make sure that the service configuration has an url that points to a valid pdl description file"+"\n"+e.getMessage());
						callbackfails = true;
					}
					if(!callbackfails && areMandatoryInputsNotNull()){
						HashMap inputValuesMap;
						PDLServiceValidation pdlServiceValidation;
						MyDefaultServiceCaller caller;
						JobsList jobsList;
						JobResult jobResult = null;
						HashMap<String, SingleParameter> outputPDLParamMap;
						HashMap<String, String> jobResultsMap;
						
						gp = pdlcontroller.getGroupProcessor();  
						
						// Resolve inputs
						inputValuesMap = getInputsMap(inputs, context, referenceService);
						
						pdlcontroller.updateUserMapperWithInputs(inputValuesMap);
											
						//end of reading inputs

						//Input values VALIDATION
						pdlServiceValidation = new PDLServiceValidation(gp);
										
						// CALL THE SERVICE
						caller = new MyDefaultServiceCaller();				
						//if the input parameters are valid
						inputsAreValid = pdlServiceValidation.isValid();
						if(inputsAreValid){
							//example call: http://pdl-calc.obspm.fr:8081/montage/TavernaCodeFrontal?mail=tetrarquis@gmail.com&NAXIS1=2259&NAXIS2=2199&CTYPE1=RA---TAN&CTYPE2=DEC--TAN&CRVAL1=210.835222357&CRVAL2=54.367562188&CRPIX1=1130&CRPIX2=1100&CDELT1=-0.000277780&CDELT2=0.000277780&CROTA2=-0.052834593&ImageLocation=SampleLocation&EQUINOX=2000

							List<String> errorList = pdlcontroller.buildErrorsList();
							if(errorList==null || errorList.size()==0){
								//get singleparameters
								HashMap<String, SingleParameter> inputSingleParams = pdlcontroller.getHashInputParameters();
								
								//create a job
								if(config.getServiceType().compareTo(config.PDLSERVICE)==0){
									
									serviceResult = caller.callService(inputSingleParams);
									
									jobsList = new JobsList();
									jobsList.parseXML(serviceResult);
									if(!jobsList.getJobs().isEmpty()){
										jobId = jobsList.getJobs().get(0).getJobId();
										userId = jobsList.getJobs().get(0).getUserId();
										
										//http://pdl-calc.obspm.fr:8081/montage/TavernaJobInfo?mail=tetrarquis@gmail.com&jobId=3&userId=7
										jobInfo = caller.getJobInfo(jobId, userId);
		
										jobResult = new JobResult();
										
										jobResult.parseXML(jobInfo);
										//System.out.println("JobOutputs: "+jobResult.getOutputParams());
										
										//if there is an error or it is aborted, the activity fails
										if(jobResult.getJobPhase()!=null)
											if(jobResult.getJobPhase().toLowerCase().compareTo(PDLServiceController.getErrorStatus())==0
													|| jobResult.getJobPhase().toLowerCase().compareTo(PDLServiceController.getAbortedStatus())==0){
												callbackfails = true;
												logger.error("The service returned "+jobResult.getJobPhase()+": "+caller.latestInvokedURL());
												callback.fail("The service returned "+jobResult.getJobPhase()+": "+caller.latestInvokedURL());
											}
										
										// TODO Update outputs in mapper and validate
		
									}else{
										callbackfails = true;
										callback.fail("Job info couldn't be parsed.");
									}
								}else{
									if(config.getServiceType().compareTo(config.RESTSERVICE)==0){
										callbackfails = true;
										logger.error("REST type of service is not yet implement for PDL descriptions");
										callback.fail("REST option is not yet implemented for PDL descriptions");
									}
								}
							}else{
								callbackfails = true;
								logger.error(errorList.toString());
								callback.fail(errorList.toString());
							}
						}else{
							//if validation fails
							callbackfails = true;
							List<String> errorList = pdlcontroller.buildErrorsList();
							if(errorList!=null && errorList.size()>0){
								logger.error(errorList.toString());
								callback.fail(errorList.toString());
							}else{
								//TODO here, I SHOULD CHECK IF THERE ARE TO COMPLETE CASES
								String status = pdlServiceValidation.validate();	
								logger.error("Error in the input parameters: \n");
								callback.fail("Error in the input parameters: \n");
							
							}
						}
						
						if(!callbackfails)
							//if(pdlServiceValidation.isValid()){
							if(inputsAreValid){
								Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
								//phase for the service:
								T2Reference simpleRef2 = referenceService.register(jobResult.getJobPhase(),0, true, context); 
								outputs.put(OUT_REPORT, simpleRef2);
								//response body
								simpleRef2 = referenceService.register(jobInfo,0, true, context); 
								outputs.put(RESPONSE_BODY, simpleRef2);
								
								
								//TODO include outputs from the service
								outputPDLParamMap = pdlcontroller.getHashOutputParameters();
								jobResultsMap = jobResult.getOutputParams();
								//if there is sth to compare
								if(outputPDLParamMap!=null && jobResultsMap!=null){
									for(Entry<String, SingleParameter> entry : outputPDLParamMap.entrySet()){
									//for(Entry<String, String> entry : jobResultsMap.entrySet()){
										String name = entry.getValue().getName();
										String value = jobResultsMap.get(name);
										if(value==null)
											value ="";
										simpleRef2 = referenceService.register(value,0, true, context); 
										outputs.put(name, simpleRef2);
										if(outputPDLParamMap.get(entry.getKey())==null){
											logger.warn(entry.getKey() + " is not in the PDL description file");
										}
									}
									
									//if there is not output in the description file I assume that there will be one
									if(outputPDLParamMap == null || outputPDLParamMap.isEmpty()){
										boolean sthInOutput = false;
										if(jobResultsMap != null && !jobResultsMap.isEmpty()){
											//if there is only one I use the default port
											if(jobResultsMap.size()==1){
												for(Entry<String, String> entry : jobResultsMap.entrySet()){
													
													simpleRef2 = referenceService.register(entry.getValue(),0, true, context);
													//System.out.println("result from xml: "+entry.getValue());
													//simpleRef2 = referenceService.register("salida",0, true, context);
													outputs.put(DEFAULT_OUTPUT, simpleRef2);
													sthInOutput = true;
												}
											}else{  //if there is more than one I use the default port for the first one and the real name for the rest
												int count = 0;
												for(Entry<String, String> entry : jobResultsMap.entrySet()){
													simpleRef2 = referenceService.register(entry.getValue(),0, true, context); 
													if(count == 0){
														outputs.put(DEFAULT_OUTPUT, simpleRef2);
														count ++;
														sthInOutput = true;
													}else{
														outputs.put(entry.getKey(), simpleRef2);
													}	
												}
											}
										}
										if(!sthInOutput){
											simpleRef2 = referenceService.register("",0, true, context);
											outputs.put(DEFAULT_OUTPUT, simpleRef2);
										}
									}
									
									
								}else{
									//logger.warn("Number of output in pdl description file doesn't match with the results provided by the user");
									logger.warn("outputPDLParamMap or jobResultsMap were null");
									callback.fail("outputPDLParamMap or jobResultsMap were null");
									callbackfails = true;
								}
								
								if(!callbackfails)
									callback.receiveResult(outputs, new int[0]);
							}else{
								logger.error("Invalid values for the input parameters, check the restrictions");
								callback.fail("Invalid values for the input parameters, check the restrictions");
							}
							
					}else{
						if(callbackfails==false){
							logger.error("Mandatory inputs are null");
							callback.fail("Mandatory inputs are null");
						}
					}
				} catch (InvalidParameterException e){
					logger.error("Invalid parameter error: "+"\n"+e.getMessage());
					callback.fail("Invalid parameter error: "+"\n"+e.getMessage());
				} catch (NullPointerException e) {
					logger.error("Problems in the run method. Is it correct the pdl-description file url?: "+ configBean.getPdlDescriptionFile()+". "+e.getMessage());
					callback.fail("Problems in the run method. Is it correct the pdl-description file url?: "+ configBean.getPdlDescriptionFile()+"\n"+e.getMessage());
				} catch (ActivityConfigurationException e) {
					logger.error("Make sure that the service configuration has an url that points to a valid pdl description file"+"\n"+e.getMessage());
					callback.fail("Make sure that the service configuration has an url that points to a valid pdl description file"+"\n"+e.getMessage());
				} catch (MalformedURLException e){
					logger.error(e.getMessage());
					callback.fail(e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("Problems parsing the resulting xml document: "+"\n"+e.getMessage());
					callback.fail("Problems parsing the resulting xml document: "+"\n"+e.getMessage());
				} catch (SAXException e) {
					logger.error("Problems parsing the resulting xml document: "+"\n"+e.getMessage());
					callback.fail("Problems parsing the resulting xml document: "+"\n"+e.getMessage());
				} catch (IOException e) {
					logger.error("Problems receiving results or parsing the resulting xml document: "+"\n"+e.getMessage());
					callback.fail("Problems receiving results or parsing the resulting xml document: "+"\n"+e.getMessage());
				} 
			}
			
		});
	}

	@Override
	public PDLServiceActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}


	public String getServiceDescription() {
		return serviceDescription;
	}
	
	public HashMap<String, SingleParameter> getHashAllParameters(){
		return this.hashAllParameters;
	}
	
	/**
	 * 
	 * @return It returns a hash with the restrictions in natural language for each group
	 */
	public HashMap<String, String> getRestrictionsOnGroups(){
		return this.restrictionsOnGroups;
	}

	
}
