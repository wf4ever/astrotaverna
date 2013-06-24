package org.purl.wf4ever.astrotaverna.aladin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import org.apache.log4j.Logger;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;

import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.visit.VisitReport.Status;
import net.sf.taverna.t2.workflowmodel.health.HealthCheck;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

/**
 * Activity configuration bean
 * @author Julian Garrido
 * @since    7 Mar 2013
 */
public class AladinScriptActivity extends
		AbstractAsynchronousActivity<AladinScriptActivityConfigurationBean>
		implements AsynchronousActivity<AladinScriptActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String FIRST_INPUT = "Script";


	private static final String OUT_STD_OUTPUT = "STD_OUTPUT";
	private static final String OUT_ERROR = "ERROR_OUTPUT";
	private static final String VO_TABLE = "VOTable";
	
	private AladinScriptActivityConfigurationBean configBean;

	private static Logger logger = Logger.getLogger(AladinScriptActivity.class);
	
	@Override
	public void configure(AladinScriptActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		
		if(!(      configBean.getTypeOfInput().compareTo("File")==0
				|| configBean.getTypeOfInput().compareTo("URL")==0
				|| configBean.getTypeOfInput().compareTo("String")==0)){
			throw new ActivityConfigurationException(
					"Invalid input type for the tables");
		}
		
		if(!(      configBean.getTypeOfMode().compareTo("gui")==0
				|| configBean.getTypeOfMode().compareTo("nogui")==0)){
			throw new ActivityConfigurationException(
					"Invalid type of process.");
		}
		
		
		// Store for getConfiguration(), but you could also make
		// getConfiguration() return a new bean from other sources
		this.configBean = configBean;

		// OPTIONAL: 
		// Do any server-side lookups and configuration, like resolving WSDLs

		// myClient = new MyClient(configBean.getExampleUri());
		// this.service = myClient.getService(configBean.getExampleString());

		
		// REQUIRED: (Re)create input/output ports depending on configuration
		configurePorts();
	}

	protected void configurePorts() {
		// In case we are being reconfigured - remove existing ports first
		// to avoid duplicates
		removeInputs();
		removeOutputs();

		// FIXME: Replace with your input and output port definitions

		// Hard coded input port, expecting a single String
		//File name for the Input tables
		addInput(FIRST_INPUT, 0, true, null, String.class);

				
		// Single value output port (depth 0)
		addOutput(OUT_STD_OUTPUT, 0);
		// Single value output port (depth 0)
		addOutput(OUT_ERROR, 0);
		addOutput(VO_TABLE, 0);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			public boolean areMandatoryInputsNotNull(){
				boolean validStatus = true;
				
				if(inputs.get(FIRST_INPUT)==null){
					validStatus = false;
				}
				
				return validStatus;
			}
			
			public void run() {
				
				boolean callbackfails=false;
				File tmpInFile = null;
				File tmpOutFile = null;
				
				if(areMandatoryInputsNotNull()){
					InvocationContext context = callback.getContext();
					ReferenceService referenceService = context.getReferenceService();
					// Resolve inputs 				
					String input = (String) referenceService.renderIdentifier(inputs.get(FIRST_INPUT), String.class, context);
					
					boolean isScriptURL = configBean.getTypeOfInput().compareTo("File")==0 || configBean.getTypeOfInput().compareTo("URL")==0;
									
					//check correct input values
					
					if(configBean.getTypeOfInput().compareTo("File")==0){
						File file = new File(input);
						if(!file.exists()){
							callback.fail("Input table file does not exist: "+ input,new IOException());
							callbackfails = true;
						}
					}
					
					if(configBean.getTypeOfInput().compareTo("URL")==0){
						try {
							URI exampleUri = new URI(input);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ input,e);
							callbackfails = true;
						}
					}
					
					
										
					// Support our configuration-dependendent input
					//boolean optionalPorts = configBean.getExampleString().equals("specialCase"); 
					
					//List<byte[]> special = null;
					// We'll also allow IN_EXTRA_DATA to be optionally not provided
					//if (optionalPorts && inputs.containsKey(IN_EXTRA_DATA)) {
					//	// Resolve as a list of byte[]
					//	special = (List<byte[]>) referenceService.renderIdentifier(
					//			inputs.get(IN_EXTRA_DATA), byte[].class, context);
					//}
					
	
					// TODO: Do the actual service invocation
	//				try {
	//					results = this.service.invoke(firstInput, special)
	//				} catch (ServiceException ex) {
	//					callback.fail("Could not invoke Stilts service " + configBean.getExampleUri(),
	//							ex);
	//					// Make sure we don't call callback.receiveResult later 
	//					return;
	//				}
					
					//Performing the work: Stilts functinalities
					String [] parameters;
					
					if(!callbackfails){

						AladinInvoker invoker = null;
						ArrayList<String> results = new ArrayList<String>();
						String table="";
						try{
							invoker = new AladinInvoker();
							AladinScriptParser parser = new AladinScriptParser();
							if(isScriptURL){
								invoker.runScriptURL(input, configBean.getTypeOfMode());
								if(configBean.getTypeOfInput().compareTo("URL")==0)
									results = parser.parseURL(input);
								else
									results = parser.parseFile(input);
							
							}else{
								invoker.runScript(input, configBean.getTypeOfMode());
								results = parser.parseScript(input);
							}
							
							table = parser.getOneColumnVOTable(results);
							
						}catch(MalformedURLException ex){
							callback.fail("There was a problem running Aladin", ex);
							logger.error("There was a problem running Aladin"+"\n"+ex.getMessage());
							callbackfails = true;
						}catch(InterruptedException ex){
							callback.fail("There was a problem running Aladin", ex);
							logger.error("There was a problem running Aladin"+"\n"+ex.getMessage());
							callbackfails = true;
						}catch(IOException ex){
							callback.fail("There was a problem running Aladin", ex);
							logger.error("There was a problem running Aladin"+"\n"+ex.getMessage());
							callbackfails = true;
						}
						
						
						if(!callbackfails){
							// Register outputs
							Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
							
							String std = "";
							String err = "";
							
							if(invoker.getError_out()!=null)
								err = invoker.getError_out();
							if(invoker.getStd_out()!=null)
								std = invoker.getStd_out();
								
							T2Reference simpleRef = referenceService.register(std, 0, true, context);
							outputs.put(OUT_STD_OUTPUT, simpleRef);
							T2Reference simpleRef2 = referenceService.register(err,0, true, context); 
							outputs.put(OUT_ERROR, simpleRef2);
							T2Reference simpleRef3 = referenceService.register(table,0, true, context); 
							outputs.put(VO_TABLE, simpleRef3);
							
							// For list outputs, only need to register the top level list
							//List<String> moreValues = new ArrayList<String>();
							//moreValues.add("Value 1");
							//moreValues.add("Value 2");
							//T2Reference moreRef = referenceService.register(moreValues, 1, true, context);
							//outputs.put(OUT_MORE_OUTPUTS, moreRef);
			
							//if (optionalPorts) {
							//	// Populate our optional output port					
							//	// NOTE: Need to return output values for all defined output ports
							//	String report = "Everything OK";
							//	outputs.put(OUT_REPORT, referenceService.register(report,
							//			0, true, context));
							//}
							
							// return map of output data, with empty index array as this is
							// the only and final result (this index parameter is used if
							// pipelining output)
							callback.receiveResult(outputs, new int[0]);
						}
					}
				}else{ //End if isthereMandatoryInputs
					callback.fail("Mandatory inputs doesn't have any value");
					callbackfails = true;
				}
			}
		});
	}

	@Override
	public AladinScriptActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}
	
	
}
