package org.purl.wf4ever.astrotaverna.aladin;

import java.io.File;
import java.io.FileWriter;
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

/* Modificaciones hechas durante las pruebas que tienen que ser eliminadas:
 * En la linea 247 invoker.runMacro es llamado con valores constantes para ubuntu. 
 * En AladinInvoker se ha comentado las lineas que capturan la salida de error y estandar. En los metodos runScript y runScriptURL
 * ---- En AladinInvoker ALADINJAR se ha puesto para ubuntu.Y AladinMacroActivityTest tambien.
 * Intentar hacer tests con un fichero macro mas sencillo. 
 * java -jar Aladin.jar -nogui script="macro Aladin_workflow_script.ajs Aladin_workflow_params.txt"
 * 
 */

/**
 * Activity configuration bean
 * @author Julian Garrido
 * @since    7 Mar 2013
 */
public class AladinMacroActivity extends
		AbstractAsynchronousActivity<AladinMacroActivityConfigurationBean>
		implements AsynchronousActivity<AladinMacroActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String FIRST_INPUT = "script";
	private static final String SECOND_INPUT = "parameters";

	private static final String OUT_STD_OUTPUT = "STD_OUTPUT";
	private static final String OUT_ERROR = "ERROR_OUTPUT";
	private static final String VO_TABLE = "VOTable";
	
	private AladinMacroActivityConfigurationBean configBean;

	private static Logger logger = Logger.getLogger(AladinMacroActivity.class);
	
	@Override
	public void configure(AladinMacroActivityConfigurationBean configBean)
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
		addInput(SECOND_INPUT, 0, true, null, String.class);
				
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
				} else if(inputs.get(SECOND_INPUT)==null){
					validStatus = false;
				}
				
				return validStatus;
			}
			
			public void run() {
				
				boolean callbackfails=false;
				File tmpInFirstFile = null;
				File tmpInSecondFile = null;
				String firstPath = "";
				String secondPath = "";
				
				if(areMandatoryInputsNotNull()){
					InvocationContext context = callback.getContext();
					ReferenceService referenceService = context.getReferenceService();
					// Resolve inputs 				
					String inputScript = (String) referenceService.renderIdentifier(inputs.get(FIRST_INPUT), String.class, context);
					String inputParams = (String) referenceService.renderIdentifier(inputs.get(SECOND_INPUT), String.class, context);
					
					boolean isScriptURL = configBean.getTypeOfInput().compareTo("File")==0 || configBean.getTypeOfInput().compareTo("URL")==0;
									
					//check correct input values
					
					if(configBean.getTypeOfInput().compareTo("File")==0){
						File file = new File(inputScript);
						if(!file.exists()){
							callback.fail("Input table file does not exist: "+ inputScript,new IOException());
							callbackfails = true;
						}
						
						file = new File(inputParams);
						if(!file.exists()){
							callback.fail("Input table file does not exist: "+ inputParams,new IOException());
							callbackfails = true;
						}
						firstPath = inputScript;
						secondPath = inputParams;
					}
					
					if(configBean.getTypeOfInput().compareTo("URL")==0){
						try {
							URI exampleUri = new URI(inputScript);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ inputScript,e);
							callbackfails = true;
						}
						try {
							URI exampleUri = new URI(inputParams);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ inputParams,e);
							callbackfails = true;
						}
						firstPath = inputScript;
						secondPath = inputParams;
					}
					
					if(configBean.getTypeOfInput().compareTo("String")==0){
						//create temp files
						try{
							tmpInFirstFile = writeStringAsTmpFile(inputScript);
							tmpInFirstFile.deleteOnExit();
							tmpInSecondFile = writeStringAsTmpFile(inputParams);
							tmpInSecondFile.deleteOnExit();
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
							callbackfails = true;
						}
						
						
						firstPath = tmpInFirstFile.getAbsolutePath();
						secondPath = tmpInSecondFile.getAbsolutePath();
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
						ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
						String table="";
						try{
							invoker = new AladinInvoker();
							AladinScriptParser parser = new AladinScriptParser();
							//invoke considering temp files or original files
							//System.out.println(firstPath);
							//System.out.println(secondPath);
							//System.out.println(configBean.getTypeOfMode());
							
							try{
								invoker.runMacro(firstPath, secondPath, configBean.getTypeOfMode());
								//invoker.runMacro("file:///Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script.ajs", "file:///Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt", "nogui");
								//invoker.runMacro("/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script_short.ajs", "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt", "nogui");
								//String example2 = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
								//invoker.runScript(example2, "nogui");
							}catch(NullPointerException ex){
								System.out.println("ERRORs from Aladin: "+ invoker.getError_out());
								System.out.println("OUTPUT from Aladin: "+ invoker.getStd_out());
								System.out.println("EXCEPTION: ");
								logger.error("Nullpointer exception from aladin");
								callbackfails = true;
								callback.fail("Error invoking Aladin");
								ex.printStackTrace();
							}
							
							if(configBean.getTypeOfInput().compareTo("URL")==0)
								results = parser.parseURLMacro(inputScript, inputParams);
							else if(configBean.getTypeOfInput().compareTo("File")==0)
								results = parser.parseFileMacro(inputScript, inputParams);
							else if(configBean.getTypeOfInput().compareTo("String")==0)
								results = parser.parseMacro(inputScript, inputParams);
							
							
							table = parser.getMultiColumnVOTable(results);
							
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
	public AladinMacroActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}
	
	private File writeStringAsTmpFile(String content) throws java.io.IOException{
	    
	    File file = File.createTempFile("astro", null);
	    FileWriter writer = new FileWriter(file);
	    writer.write(content);
	    writer.close();
	    
	    return file;
	}
	
}
