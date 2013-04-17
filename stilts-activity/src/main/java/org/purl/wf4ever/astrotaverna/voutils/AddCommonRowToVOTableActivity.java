package org.purl.wf4ever.astrotaverna.voutils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
//comment from terminal
import org.purl.wf4ever.astrotaverna.utils.MyUtils;
import org.purl.wf4ever.astrotaverna.utils.NoExitSecurityManager;

import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.ttools.Stilts;

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

public class AddCommonRowToVOTableActivity extends
		AbstractAsynchronousActivity<AddCommonRowToVOTableActivityConfigurationBean>
		implements AsynchronousActivity<AddCommonRowToVOTableActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String IN_FIRST_INPUT = "commonRowVOTable";
	private static final String IN_SECOND_INPUT = "mainVOTable";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	
	private static final String OUT_SIMPLE_OUTPUT = "VOTable";
	private static final String OUT_REPORT = "report";
	
	private AddCommonRowToVOTableActivityConfigurationBean configBean;

	private static Logger logger = Logger.getLogger(AddCommonRowToVOTableActivity.class);
	
	@Override
	public void configure(AddCommonRowToVOTableActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		//if (!configBean.getTablefile1().exists()) {
		//	throw new ActivityConfigurationException(
		//			"Input table file 1 doesn't exist");
		//}
		
		if(!(      configBean.getTypeOfInput().compareTo("File")==0
				|| configBean.getTypeOfInput().compareTo("URL")==0
				|| configBean.getTypeOfInput().compareTo("String")==0)){
			throw new ActivityConfigurationException("Invalid input type for the tables");
		}
		if(!(configBean.getCommonRowPosition().compareTo("Left")==0
				|| configBean.getCommonRowPosition().compareTo("Right")==0)){
			throw new ActivityConfigurationException("Invalid common row position.");
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
		addInput(IN_FIRST_INPUT, 0, true, null, String.class);
		addInput(IN_SECOND_INPUT, 0, true, null, String.class);
		
		//File name for the output table
		if(configBean.getTypeOfInput().compareTo("File")==0){
			addInput(IN_OUTPUT_TABLE_NAME, 0, true, null, String.class);
		}
		
		// Single value output port (depth 0)
		addOutput(OUT_SIMPLE_OUTPUT, 0);
		// Single value output port (depth 0)
		addOutput(OUT_REPORT, 0);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeAsynch(final Map<String, T2Reference> inputs,
			final AsynchronousActivityCallback callback) {
		// Don't execute service directly now, request to be run ask to be run
		// from thread pool and return asynchronously
		callback.requestRun(new Runnable() {
			
			/*
			 * Check if the mandatory inputs are not null
			 */
			public boolean areMandatoryInputsNotNull(){
				boolean validStatus = true;
				try{
					if(inputs.get(IN_FIRST_INPUT)==null
							|| inputs.get(IN_SECOND_INPUT)==null)
						validStatus = false;
					else if(configBean.getTypeOfInput().compareTo("File")==0 
							&& inputs.get(IN_OUTPUT_TABLE_NAME)==null)
						validStatus = false;
				}catch(Exception ex){validStatus = false;}
				
				return validStatus;
			}
			
			public void run() {
				boolean callbackfails=false;
				File firstFile = null;
				File secondFile = null;
				File outputFile = null;
				URI firstURI = null;
				URI secondURI = null;
				boolean leftPosition=true;
				
				if(areMandatoryInputsNotNull()){
				
					InvocationContext context = callback
							.getContext();
					ReferenceService referenceService = context
							.getReferenceService();
					// Resolve inputs 				
					String firstInput = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT), 
							String.class, context);
					String secondInput = (String) referenceService.renderIdentifier(inputs.get(IN_SECOND_INPUT), 
							String.class, context);
					
					boolean optionalPorts = configBean.getTypeOfInput().compareTo("File")==0;
					
					leftPosition = configBean.getCommonRowPosition().compareTo("Left")==0;
					
					String outputTableName = null;
					if(optionalPorts && inputs.containsKey(IN_OUTPUT_TABLE_NAME)){ //configBean.getNumberOfTables()==3
						outputTableName = (String) referenceService.renderIdentifier(inputs.get(IN_OUTPUT_TABLE_NAME), 
								String.class, context);
					}
	
					
					
					if(configBean.getTypeOfInput().compareTo("File")==0){
						firstFile = new File(firstInput);
						if(!firstFile.exists()){
							callback.fail("Input table file does not exist: "+ firstInput,new IOException());
							callbackfails = true;
						}
						secondFile = new File(secondInput);
						if(!secondFile.exists()){
							callback.fail("Input table file does not exist: "+ secondInput,new IOException());
							callbackfails = true;
						}
						outputFile = new File(outputTableName);
						if(!outputFile.exists()){
							callback.fail("Output table file could not be created: "+ outputTableName,new IOException());
							callbackfails = true;
						}
					}
					
					
					if(configBean.getTypeOfInput().compareTo("URL")==0){
						try {
							firstURI = new URI(firstInput);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ firstInput,e);
							callbackfails = true;
						}
						try {
							secondURI = new URI(secondInput);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ secondInput,e);
							callbackfails = true;
						}
					}
					
					
					//prepare tmp input files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0){
						try{
							firstFile = MyUtils.writeStringAsTmpFile(firstInput);
							firstFile.deleteOnExit();
							firstInput = firstFile.getAbsolutePath();
							
							secondFile = MyUtils.writeStringAsTmpFile(secondInput);
							secondFile.deleteOnExit();
							secondInput = secondFile.getAbsolutePath();
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
							callbackfails = true;
						}
					}
					
					//prepare tmp output files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0
							|| configBean.getTypeOfInput().compareTo("URL")==0){
						try{
							outputFile = File.createTempFile("astro", null);
							outputFile.deleteOnExit();
							outputTableName = outputFile.getAbsolutePath();
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
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
						
						AddCommonRowToVOTableController controller;
						
						try {
							if(configBean.getTypeOfInput().compareTo("String")==0
									|| configBean.getTypeOfInput().compareTo("File")==0){
								controller = new AddCommonRowToVOTableController(firstFile, secondFile, leftPosition);
								controller.writeJoinTable(outputFile);
							}else{
								if(configBean.getTypeOfInput().compareTo("URL")==0){
									controller = new AddCommonRowToVOTableController(firstURI, secondURI, leftPosition);
									controller.writeJoinTable(outputFile);
								}else{
									callbackfails=true;
									logger.error("Invalid type of input at "+ this.getClass().getName());
									callback.fail("Invalid type of input");
								}
							}
						} catch (TableFormatException e) {
							callbackfails=true;
							logger.error("Invalid type of input at "+ this.getClass().getName()+". "+e.getMessage());
							callback.fail("Invalid type of input: "+ e.getMessage());
						} catch (IOException e) {
							callbackfails=true;
							logger.error("Invalid type of input at "+ this.getClass().getName()+". "+e.getMessage());
							callback.fail("Invalid type of input: "+ e.getMessage());
						}
						
												
						if(!callbackfails){
							// Register outputs
							Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
							String simpleValue = "";// //Name of the output file or result
							String simpleoutput = "valid";
							
							if(optionalPorts){ //case File
								simpleValue = outputTableName;
							}else if(configBean.getTypeOfInput().compareTo("URL")==0
										|| configBean.getTypeOfInput().compareTo("String")==0){
						
								try{
									simpleValue = MyUtils.readFileAsString(outputFile.getAbsolutePath());
								}catch (Exception ex){
									callback.fail("It wasn't possible to read the result. ", ex);
									callbackfails = true;
								}
							}
							if(!callbackfails){
								T2Reference simpleRef = referenceService.register(simpleValue, 0, true, context);
								outputs.put(OUT_SIMPLE_OUTPUT, simpleRef);
								T2Reference simpleRef2 = referenceService.register(simpleoutput,0, true, context); 
								outputs.put(OUT_REPORT, simpleRef2);
				
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
					}
				}
			}
		});
	}

	@Override
	public AddCommonRowToVOTableActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
