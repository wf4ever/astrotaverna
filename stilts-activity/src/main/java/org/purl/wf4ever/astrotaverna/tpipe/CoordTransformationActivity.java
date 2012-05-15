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

import org.apache.commons.io.IOUtils;
import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.utils.MyUtils;

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

public class CoordTransformationActivity extends
		AbstractAsynchronousActivity<CoordTransformationActivityConfigurationBean>
		implements AsynchronousActivity<CoordTransformationActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String IN_FIRST_INPUT_TABLE = "firstTable";
	private static final String IN_FORMAT_INPUT_TABLE = "formatTableIn";
	private static final String IN_FORMAT_OUTPUT_TABLE = "formatTableOut";
	private static final String IN_NAME_NEW_COL = "nameNewCol";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private CoordTransformationActivityConfigurationBean configBean;

	@Override
	public void configure(CoordTransformationActivityConfigurationBean configBean)
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
		
		if(!isAllowedCoordenatesFunction(configBean.getTypeOfFilter())){
			throw new ActivityConfigurationException(
					"Invalid function name");
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
		addInput(IN_FORMAT_INPUT_TABLE, 0, true, null, String.class);
		addInput(IN_FORMAT_OUTPUT_TABLE, 0, true, null, String.class);
		addInput(IN_NAME_NEW_COL, 0, true, null, String.class);
		
		
		if(configBean.getTypeOfInput().compareTo("File")==0){
			addInput(IN_OUTPUT_TABLE_NAME, 0, true, null, String.class);
		}
		
		
		//coordenates parameters
		Vector<String> inParams = getNameParamsOfCoordFunctions(configBean.getTypeOfFilter());
		if(inParams!=null)
			for(String param : inParams){
				addInput(param, 0, true, null, String.class);
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
			
			public void run() {
				Vector<String> inParams;
				Vector<String> paramValues;
				boolean callbackfails=false;
				InvocationContext context = callback.getContext();
				ReferenceService referenceService = context.getReferenceService();
				// Resolve inputs 				
				String inputTable = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT_TABLE), String.class, context);
				String formatInputTable = (String) referenceService.renderIdentifier(inputs.get(IN_FORMAT_INPUT_TABLE), String.class, context);
				String formatOutputTable= (String) referenceService.renderIdentifier(inputs.get(IN_FORMAT_OUTPUT_TABLE), String.class, context);
				String nameNewCol = (String) referenceService.renderIdentifier(inputs.get(IN_NAME_NEW_COL), String.class, context);
				
				
				boolean optionalPorts = configBean.getTypeOfInput().compareTo("File")==0;
				
				String outputTableName = null;
				if(optionalPorts && inputs.containsKey(IN_OUTPUT_TABLE_NAME)){ //configBean.getNumberOfTables()==3
					outputTableName = (String) referenceService.renderIdentifier(inputs.get(IN_OUTPUT_TABLE_NAME), 
							String.class, context);
				}
				
				inParams = getNameParamsOfCoordFunctions(configBean.getTypeOfFilter());
				paramValues = new Vector<String>();
				if(inParams!=null && !inParams.isEmpty()){
					for(String param : inParams)
						if(inputs.containsKey(param))
							paramValues.add((String) referenceService.renderIdentifier(inputs.get(param), 
									String.class, context));
				}else{
					callback.fail("Lack of params in the coordenates function",new Exception());
					callbackfails = true;
				}
					
				

				//include default values if empty inputs
				//default format => votable
				if(formatInputTable == null || formatInputTable.trim().isEmpty()){
					formatInputTable = "votable";
				}
				if(formatOutputTable == null || formatOutputTable.trim().isEmpty()){
					formatOutputTable = "votable";
				}
				if(nameNewCol==null || nameNewCol.isEmpty())
					nameNewCol="NEWCOL";
				
				//check correct input values
				if(!MyUtils.isValidInputFormat(formatInputTable)){
					callback.fail("Invalid input table format: "+ formatInputTable,new IOException());
					callbackfails = true;
				}
				
				
				if(!MyUtils.isValidOutputFormat(formatOutputTable)){
					callback.fail("Invalid output table format: "+ formatOutputTable,new IOException());
					callbackfails = true;
				}
				
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
				if(inParams.size()!=paramValues.size()){
					callback.fail("Expected number of parameters for the function: "+ inParams.size()+".\nReceived number of paramaters: "+ paramValues.size(),new Exception());
					callbackfails = true;
				}
				if(paramValues.size()>0){
					boolean nullvalues = false;
					for(int i = 0; i<paramValues.size() && !nullvalues;i++)
						if(paramValues.elementAt(i)==null || paramValues.elementAt(i).isEmpty()){
							nullvalues = true;
							callback.fail("Function parameters are empty",new Exception());
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
					String  functionName;
					String commaSeparatedValues;
					
					//handling redirection of standard input and output
					PrintStream out = System.out;
					PrintStream stdout = System.out;
					InputStream in = System.in;
					InputStream stdin = System.in;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					out = new PrintStream(baos);
					
					
					for( Object p : paramValues){
						boolean isinstance = false;
						if(p instanceof String)
							isinstance = true;
						String name = p.getClass().getName();
						name = "";
						
					}
					
					commaSeparatedValues = MyUtils.toCommaSeparatedString(paramValues);
					functionName = ((Map<String, String>)CoordTransformationActivity.getFunctionsNameMap()).get(configBean.getTypeOfFilter());
					
					if(optionalPorts){ //case File
						parameters = new String[6];
						parameters[0] = "tpipe";
						parameters[1] = "ifmt="+formatInputTable;
						parameters[2] = "in="+inputTable;
						parameters[3] = "ofmt="+formatOutputTable;
						parameters[4] = "cmd=addcol "+ nameNewCol +" '(" + functionName + "("+ commaSeparatedValues +"))'";
						//System.out.println(parameters[4]);
						//parameters[4] = "cmd=addcol newCol '(raFK4toFK5radians(U, R))'";
						parameters[5] = "out="+outputTableName;
					}else if(configBean.getTypeOfInput().compareTo("Query")==0 
								||configBean.getTypeOfInput().compareTo("URL")==0){
							
						parameters = new String[5];
						parameters[0] = "tpipe";
						parameters[1] = "ifmt="+formatInputTable;
						parameters[2] = "in="+inputTable;
						parameters[3] = "ofmt="+formatOutputTable;
						parameters[4] = "cmd=addcol "+ nameNewCol +" '(" + functionName + "("+ commaSeparatedValues +"))'";
						//Redirecting output
						System.setOut(out);
					}else if(configBean.getTypeOfInput().compareTo("String")==0){
						parameters = new String[5];
						parameters[0] = "tpipe";
						parameters[1] = "ifmt="+formatInputTable;
						parameters[2] = "in=-";
						parameters[3] = "ofmt="+formatOutputTable;
						parameters[4] = "cmd=addcol "+ nameNewCol +" '(" + functionName + "("+ commaSeparatedValues +"))'";
						//Redirecting output and input
						in = IOUtils.toInputStream(inputTable);
						//Optionally, do this: 
						//InputStream is = new ByteArrayInputStream(resultTable.getBytes( charset ) );
						System.setIn(in);
						System.setOut(out);
					}else{
						parameters = new String[5];
						parameters[0] = "tpipe";
						parameters[1] = "ifmt="+formatInputTable;
						parameters[2] = "in=-";
						parameters[3] = "ofmt="+formatOutputTable;
	
						//Redirecting output and input
						in = IOUtils.toInputStream(inputTable);
						//Optionally, do this: 
						//InputStream is = new ByteArrayInputStream(resultTable.getBytes( charset ) );
						System.setIn(in);
						System.setOut(out);
					}
						
					System.setProperty("votable.strict", "false");
					Stilts.main(parameters);
						
					
					
					// Register outputs
					Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
					String simpleValue = "/home/julian/Documents/wf4ever/tables/resultTable.ascii";// //Name of the output file or result
					String simpleoutput = "simple-report";
					
					if(optionalPorts){ //case File
						simpleValue = outputTableName;
					}else if(configBean.getTypeOfInput().compareTo("Query")==0 
								||configBean.getTypeOfInput().compareTo("URL")==0){
				
						out.close();
						if(out.checkError()){
							simpleoutput += "Output redirection failed.\n";
						}
						
						simpleValue = baos.toString();
						System.setOut(stdout);	
						
					}else if(configBean.getTypeOfInput().compareTo("String")==0){
						out.close();
						if(out.checkError()){
							simpleoutput += "Output redirection failed.\n";
						}
						
						simpleValue = baos.toString();
						System.setOut(stdout);	
						
						try {
							in.close();
						} catch (IOException e) {
							simpleoutput += "Input redirection failed.\n" + e.toString();
						}
						System.setIn(stdin);
					}else{
						out.close();
						if(out.checkError()){
							simpleoutput += "Output redirection failed.\n";
						}
						
						simpleValue = baos.toString();
						System.setOut(stdout);	
						
						try {
							in.close();
						} catch (IOException e) {
							simpleoutput += "Input redirection failed.\n" + e.toString();
						}
						System.setIn(stdin);
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
		});
	}

	@Override
	public CoordTransformationActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

	/*
	 * Returns a Vector that contains the implemented functions
	 */
	static Vector<String> getListOfCoordenatesFunctions(){
		String [] array1parameters ={"radiansToDms", "radiansToHms", "dmsToRadians", "hmsToRadians", "hoursToRadians", "degreesToRadians", "radiansToDegrees"};
		String [] array2parameters ={"raFK4toFK5radians2", "decFK4toFK5radians2", "raFK5toFK4radians2", "decFK5toFK4radians2"};
		String [] array3parameters ={"raFK4toFK5Radians3", "decFK4toFK5Radians3", "raFK5toFK4Radians3", "decFK5toFK4Radians3"};
		String [] array4parameters ={"skyDistanceRadians"};
		
		Vector<String> vAll = new Vector (Arrays.asList(array1parameters));
		vAll.addAll(Arrays.asList(array2parameters));
		vAll.addAll(Arrays.asList(array3parameters));
		vAll.addAll(Arrays.asList(array4parameters));

		return vAll;
	}
	
	public boolean isAllowedCoordenatesFunction(String name){
		Vector<String> functions = this.getListOfCoordenatesFunctions();
		boolean found=false;
		Iterator<String> it = functions.iterator();
	
		while( it.hasNext() && !found ){
			if(it.next().compareTo(name)==0)
				found = true;
		}
		
		return found;
	}
	
	/*
	 * Returns an array that contains the inputs names for each function.
	 */
	/*
	 * Returns an array that contains the inputs names for each function.
	 */
	static Vector<String> getNameParamsOfCoordFunctions(String coordenatesFunction){
		Vector<String> params = new Vector<String>();
		
		
		if(coordenatesFunction.compareTo("radiansToDms")==0
				|| coordenatesFunction.compareTo("radiansToHms")==0
				|| coordenatesFunction.compareTo("hoursToRadians")==0
				|| coordenatesFunction.compareTo("degreesToRadians")==0
				|| coordenatesFunction.compareTo("radiansToDegrees")==0){
			
			params.add("value");
			
		}else if(coordenatesFunction.compareTo("dmsToRadians")==0
				|| coordenatesFunction.compareTo("hmsToRadians")==0){
			
			params.add("value");
			
		}else if(coordenatesFunction.compareTo("raFK4toFK5radians2")==0
				|| coordenatesFunction.compareTo("decFK4toFK5radians2")==0
				|| coordenatesFunction.compareTo("raFK5toFK4radians2")==0
				|| coordenatesFunction.compareTo("decFK5toFK4radians2")==0){

			params.add("RA");
			params.add("DEC");
			
		}else if(coordenatesFunction.compareTo("raFK4toFK5radians3")==0
				|| coordenatesFunction.compareTo("decFK4toFK5radians3")==0
				|| coordenatesFunction.compareTo("raFK5toFK4radians3")==0
				|| coordenatesFunction.compareTo("decFK5toFK4radians3")==0){
			
			params.add("RA");
			params.add("DEC");
			params.add("bepoch");
			
		} else if(coordenatesFunction.compareTo("skyDistanceRadians")==0){
			
			params.add("RA1");
			params.add("DEC1");
			params.add("RA2");
			params.add("DEC2");
			
		} else {
			params.add("value");
		}
			
		
		return params;
	}
	
	static Map<String, String> getFunctionsNameMap(){
		//<UI function name, real function name> 
		Map<String, String> mapping = new HashMap<String, String>();
		
		String [] array1parameters ={"radiansToDms", "radiansToHms", "dmsToRadians", "hmsToRadians", "hoursToRadians", "degreesToRadians", "radiansToDegrees"};
		String [] array2parameters ={"raFK4toFK5radians2", "decFK4toFK5radians2", "raFK5toFK4radians2", "decFK5toFK4radians2"};
		String [] array3parameters ={"raFK4toFK5Radians3", "decFK4toFK5Radians3", "raFK5toFK4Radians3", "decFK5toFK4Radians3"};
		String [] array4parameters ={"skyDistanceRadians"};
		mapping.put("radiansToDms", "radiansToDms");
		mapping.put("radiansToHms", "radiansToHms");
		mapping.put("dmsToRadians", "dmsToRadians");
		mapping.put("hmsToRadians", "hmsToRadians");
		mapping.put("hoursToRadians", "hoursToRadians");
		mapping.put("degreesToRadians", "degreesToRadians");
		mapping.put("radiansToDegrees", "radiansToDegrees");
		
		mapping.put("raFK4toFK5radians2", "raFK4toFK5radians");
		mapping.put("decFK4toFK5radians2", "decFK4toFK5radians");
		mapping.put("raFK5toFK4radians2", "raFK5toFK4radians");
		mapping.put("decFK5toFK4radians2", "decFK5toFK4radians");
		
		mapping.put("raFK4toFK5radians3", "raFK4toFK5radians");
		mapping.put("decFK4toFK5radians3", "decFK4toFK5radians");
		mapping.put("raFK5toFK4radians3", "raFK5toFK4radians");
		mapping.put("decFK5toFK4radians3", "decFK5toFK4radians");
		
		mapping.put("skyDistanceRadians", "skyDistanceRadians");

		return mapping;
		
	}
	
	
/*	static Vector<Parameter<String, Object>> getNameParamsOfCoordFunctions(String coordenatesFunction){
		Vector<Parameter<String, Object>> params = new Vector<Parameter<String,Object>>();
		Parameter<String, Object> result;
		
		if(coordenatesFunction.compareTo("radiansToDms")==0
				|| coordenatesFunction.compareTo("radiansToHms")==0
				|| coordenatesFunction.compareTo("hoursToRadians")==0
				|| coordenatesFunction.compareTo("degreesToRadians")==0
				|| coordenatesFunction.compareTo("radiansToDegrees")==0){
			
			result = new Parameter<String, Object>("value", float.class);
			params.add(result);
			
		}else if(coordenatesFunction.compareTo("dmsToRadians")==0
				|| coordenatesFunction.compareTo("hmsToRadians")==0){
			
			result = new Parameter<String, Object>("value", String.class);
			params.add(result);
			
		}else if(coordenatesFunction.compareTo("raFK4toFK5radians2")==0
				|| coordenatesFunction.compareTo("decFK4toFK5radians2")==0
				|| coordenatesFunction.compareTo("raFK5toFK4radians2")==0
				|| coordenatesFunction.compareTo("decFK5toFK4radians2")==0){

			result = new Parameter("RA", float.class);
			params.add(result);
			result = new Parameter("DEC", float.class);
			params.add(result);
			
		}else if(coordenatesFunction.compareTo("raFK4toFK5radians3")==0
				|| coordenatesFunction.compareTo("decFK4toFK5radians3")==0
				|| coordenatesFunction.compareTo("raFK5toFK4radians3")==0
				|| coordenatesFunction.compareTo("decFK5toFK4radians3")==0){
			
			result = new Parameter("RA", float.class);
			params.add(result);
			result = new Parameter("DEC", float.class);
			params.add(result);
			result = new Parameter("bepoch", float.class);
			params.add(result);
			
		} if(coordenatesFunction.compareTo("skyDistanceRadians")==0){
			
			result = new Parameter("RA1", float.class);
			params.add(result);
			result = new Parameter("DEC1", float.class);
			params.add(result);
			result = new Parameter("RA2", float.class);
			params.add(result);
			result = new Parameter("DEC2", float.class);
			params.add(result);
			
		} else {
			result = new Parameter("value", String.class);
			params.add(result);
		}
			
		
		return params;
	}
	*/
	/*
	public static class Parameter<String,Object> {
	    private String name;
	    private Object className;
	    public Parameter(String name, Object className){
	        this.name = name;
	        this.className = className;
	    }

	    public String getName(){ return name; }
	    public Object getClassName(){ return className; }
	    public void setName(String name){ this.name = name; }
	    public void setClassName(Object className){ this.className = className; }
	}

	*/
	
	
}
