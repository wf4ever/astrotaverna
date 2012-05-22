package org.purl.wf4ever.astrotaverna.tpipe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.utils.MyUtils;
import org.purl.wf4ever.astrotaverna.utils.NoExitSecurityManager;

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

/**
 * Activity configuration bean
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class AddSkyCoordsActivity extends
		AbstractAsynchronousActivity<AddSkyCoordsActivityConfigurationBean>
		implements AsynchronousActivity<AddSkyCoordsActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	private static final String IN_UNITS = "units";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private AddSkyCoordsActivityConfigurationBean configBean;

	@Override
	public void configure(AddSkyCoordsActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		//if (!configBean.getTablefile1().exists()) {
		//	throw new ActivityConfigurationException(
		//			"Input table file 1 doesn't exist");
		//}
		
		if(!(      configBean.getTypeOfInput().compareTo("File")==0
				|| configBean.getTypeOfInput().compareTo("Query")==0
				|| configBean.getTypeOfInput().compareTo("URL")==0
				|| configBean.getTypeOfInput().compareTo("String")==0)){
			throw new ActivityConfigurationException(
					"Invalid input type for the tables");
		}
		
		if(!(      configBean.getTypeOfOutSystem().compareTo("icrs")==0
				|| configBean.getTypeOfOutSystem().compareTo("fk4")==0
				|| configBean.getTypeOfOutSystem().compareTo("fk5")==0
				|| configBean.getTypeOfOutSystem().compareTo("galactic")==0
				|| configBean.getTypeOfOutSystem().compareTo("supergalactic")==0
				|| configBean.getTypeOfOutSystem().compareTo("ecliptic")==0)){
			throw new ActivityConfigurationException(
					"Invalid coordinates system for the input columns");
		}
		
		if(!(      configBean.getTypeOfInSystem().compareTo("icrs")==0
				|| configBean.getTypeOfInSystem().compareTo("fk4")==0
				|| configBean.getTypeOfInSystem().compareTo("fk5")==0
				|| configBean.getTypeOfInSystem().compareTo("galactic")==0
				|| configBean.getTypeOfInSystem().compareTo("supergalactic")==0
				|| configBean.getTypeOfInSystem().compareTo("ecliptic")==0)){
			throw new ActivityConfigurationException(
					"Invalid coordinates system for the output columns");
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
		addInput(IN_FIRST_INPUT_TABLE, 0, true, null, String.class);
		addInput(IN_UNITS, 0, true, null, String.class);
		
		
		if(configBean.getTypeOfInput().compareTo("File")==0){
			addInput(IN_OUTPUT_TABLE_NAME, 0, true, null, String.class);
		}
		
		
		//coordinates parameters
		Vector<String> inParams = getNameParamsOfCoordSystems(configBean.getTypeOfInSystem());
		if(inParams!=null)
			for(String param : inParams){
				addInput(param+"In", 0, true, null, String.class);
			}

		Vector<String> outParams = getNameParamsOfCoordSystems(configBean.getTypeOfOutSystem());
		if(outParams!=null)
			for(String param : outParams){
				addInput(param+"Out", 0, true, null, String.class);
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
			
			public boolean areMandatoryInputsNotNull(){
				boolean validStatus = true;
				
				if(inputs.get(IN_FIRST_INPUT_TABLE)==null
						|| inputs.get(IN_UNITS)==null){
					validStatus = false;
				}else{
					if(configBean.getTypeOfInput().compareTo("File")==0 
						&& inputs.get(IN_OUTPUT_TABLE_NAME)==null){
						validStatus = false;
					} else{
						Vector<String> inParams = getNameParamsOfCoordSystems(configBean.getTypeOfInSystem());
						if(inParams!=null)
							for(String param : inParams){
								if(inputs.get(param+"In")==null)
									validStatus = false;
							}
						Vector<String> outParams = getNameParamsOfCoordSystems(configBean.getTypeOfOutSystem());
						if(outParams!=null)
							for(String param : outParams){
								if(inputs.get(param+"Out")==null)
									validStatus = false;
							}
					}
				}
				
				return validStatus;
			}
			
			public void run() {
				Vector<String> params;
				Vector<String> inParamValues;
				Vector<String> outParamValues;
				boolean callbackfails=false;
				File tmpInFile = null;
				File tmpOutFile = null;
				
				if(areMandatoryInputsNotNull()){
					InvocationContext context = callback.getContext();
					ReferenceService referenceService = context.getReferenceService();
					// Resolve inputs 				
					String inputTable = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT_TABLE), String.class, context);
					String units = (String) referenceService.renderIdentifier(inputs.get(IN_UNITS), String.class, context);
					
					
					boolean optionalPorts = configBean.getTypeOfInput().compareTo("File")==0;
					
					String outputTableName = null;
					if(optionalPorts && inputs.containsKey(IN_OUTPUT_TABLE_NAME)){ //configBean.getNumberOfTables()==3
						outputTableName = (String) referenceService.renderIdentifier(inputs.get(IN_OUTPUT_TABLE_NAME), 
								String.class, context);
					}
					
					params = getNameParamsOfCoordSystems(configBean.getTypeOfInSystem());
					inParamValues = new Vector<String>();
					if(params!=null && !params.isEmpty()){
						for(String param : params)
							if(inputs.containsKey(param+"In"))
								inParamValues.add((String) referenceService.renderIdentifier(inputs.get(param+"In"), 
										String.class, context));
					}else{
						callback.fail("Lack of params for the coordinates systems",new Exception());
						callbackfails = true;
					}
						
					params = getNameParamsOfCoordSystems(configBean.getTypeOfOutSystem());
					outParamValues = new Vector<String>();
					if(params!=null && !params.isEmpty()){
						for(String param : params)
							if(inputs.containsKey(param+"Out"))
								outParamValues.add((String) referenceService.renderIdentifier(inputs.get(param+"Out"), 
										String.class, context));
					}else{
						callback.fail("Lack of params for the coordinates systems",new Exception());
						callbackfails = true;
					}
	
					//check correct input values
					
					if(configBean.getTypeOfInput().compareTo("File")==0){
						File file = new File(inputTable);
						if(!file.exists()){
							callback.fail("Input table file does not exist: "+ inputTable,new IOException());
							callbackfails = true;
						}
					}
					
					if(configBean.getTypeOfInput().compareTo("URL")==0){
						try {
							URI exampleUri = new URI(inputTable);
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ inputTable,e);
							callbackfails = true;
						}
					}
					
					if(!(units.compareTo("degrees")==0
							|| units.compareTo("radians")==0
							|| units.compareTo("sexagesimal")==0)){
						callback.fail("Invalid units "+ units);
						callbackfails = true;
					}
					
					//check variable params of the function
					if(inParamValues.size()!=2 || outParamValues.size()!=2){
						callback.fail("Expected number of parameters for the coordinates system: 2");
						callbackfails = true;
					}
					if(inParamValues.size()>0){
						boolean nullvalues = false;
						for(int i = 0; i<inParamValues.size() && !nullvalues;i++)
							if(inParamValues.elementAt(i)==null || inParamValues.elementAt(i).isEmpty()){
								nullvalues = true;
								callback.fail("Coordinates inputs are empty",new Exception());
								callbackfails = true;
							}
					}
					if(outParamValues.size()>0){
						boolean nullvalues = false;
						for(int i = 0; i<outParamValues.size() && !nullvalues;i++)
							if(outParamValues.elementAt(i)==null || outParamValues.elementAt(i).isEmpty()){
								nullvalues = true;
								callback.fail("Coordinates inputs are empty",new Exception());
								callbackfails = true;
							}
					}
					
					
					//prepare tmp input files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0){
						try{
							tmpInFile = MyUtils.writeStringAsTmpFile(inputTable);
							tmpInFile.deleteOnExit();
							inputTable = tmpInFile.getAbsolutePath();
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
							callbackfails = true;
						}
					}
					
					//prepare tmp output files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0
							|| configBean.getTypeOfInput().compareTo("URL")==0
							|| configBean.getTypeOfInput().compareTo("Query")==0){
						try{
							tmpOutFile = File.createTempFile("astro", null);
							tmpOutFile.deleteOnExit();
							outputTableName = tmpOutFile.getAbsolutePath();
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
						String  inSystem, outSystem, skycmdparam;
						
						inSystem = ((Map<String, String>)AddSkyCoordsActivity.getSystemsNameMap()).get(configBean.getTypeOfInSystem());
						outSystem = ((Map<String, String>)AddSkyCoordsActivity.getSystemsNameMap()).get(configBean.getTypeOfOutSystem());
						
						
						skycmdparam = "-inunit "+ units + " -outunit "+ units + " "+ inSystem + " " + outSystem;
						for(String param : inParamValues)
							skycmdparam += " " + param;
						
						for(String param : outParamValues)
							skycmdparam += " " + param;
						
						parameters = new String[6];
						parameters[0] = "tpipe";
						parameters[1] = "ifmt=votable";
						parameters[2] = "in="+inputTable;
						parameters[3] = "ofmt=votable";
						parameters[4] = "cmd=addskycoords " + skycmdparam;
						//parameters[4] = "cmd=addskycoords -inunit deg -outunit deg fk4 galactic ra dec longitude latitude";
						parameters[5] = "out="+outputTableName;
						
							
						SecurityManager securityBackup = System.getSecurityManager();
						System.setSecurityManager(new NoExitSecurityManager());
						
						try{
							System.setProperty("votable.strict", "false");
							Stilts.main(parameters);
						}catch(SecurityException ex){
							callback.fail("Invalid service call: check the input parameters", ex);
							callbackfails = true;
						}
					
						System.setSecurityManager(securityBackup);
						
						if(!callbackfails){
							// Register outputs
							Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
							String simpleValue = "";// //Name of the output file or result
							String simpleoutput = "simple-report";
							
							if(optionalPorts){ //case File
								simpleValue = outputTableName;
							}else if(configBean.getTypeOfInput().compareTo("Query")==0 
										||configBean.getTypeOfInput().compareTo("URL")==0
										|| configBean.getTypeOfInput().compareTo("String")==0){
								
								try{
									simpleValue = MyUtils.readFileAsString(tmpOutFile.getAbsolutePath());
								}catch (Exception ex){
									callback.fail("It wasn't possible to read the result from a temporary file", ex);
									callbackfails = true;
								}
								
							}
			
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
				}else{ //End if isthereMandatoryInputs
					callback.fail("Mandatory inputs doesn't have any value");
					callbackfails = true;
				}
			}
		});
	}

	@Override
	public AddSkyCoordsActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

	/*
	 * Returns a Vector that contains the implemented functions
	 */
	public Vector<String> getListOfCoordinatesSystems(){
		String [] array1parameters ={"icrs", "fk4", "fk5", "galactic", "supergalactic", "ecliptic"};
		
		Vector<String> vAll = new Vector (Arrays.asList(array1parameters));
		
		return vAll;
	}
	
	public boolean isAllowedCoordinatesSystem(String name){
		Vector<String> functions = this.getListOfCoordinatesSystems();
		boolean found=false;
		Iterator<String> it = functions.iterator();
	
		while( it.hasNext() && !found ){
			if(it.next().compareTo(name)==0)
				found = true;
		}
		
		return found;
	}
	

	/*
	 * Returns an array that contains the inputs parameters name for each function.
	 */
	static Vector<String> getNameParamsOfCoordSystems(String coordinatesSystem){
		Vector<String> params = new Vector<String>();
		
		
		if(coordinatesSystem.compareTo("icrs")==0
				|| coordinatesSystem.compareTo("fk4")==0
				|| coordinatesSystem.compareTo("fk5")==0){
			
			params.add("nameRA");
			params.add("nameDEC");
			
		}else if(coordinatesSystem.compareTo("galactic")==0
				|| coordinatesSystem.compareTo("supergalactic")==0
				|| coordinatesSystem.compareTo("ecliptic")==0){

			params.add("nameLong");
			params.add("nameLat");
			
		}
			
		
		return params;
	}
	
	/*
	 * mapping between the name in the user interface and the real function name
	 */
	static Map<String, String> getSystemsNameMap(){
		//<UI function name, real function name> 
		Map<String, String> mapping = new HashMap<String, String>();
		
		mapping.put("icrs", "icrs");
		mapping.put("fk4", "fk4");
		mapping.put("fk5", "fk5");
		mapping.put("galactic", "galactic");
		mapping.put("supergalactic", "supergalactic");
		mapping.put("ecliptic", "ecliptic");

		return mapping;
		
	}
	
	
}
