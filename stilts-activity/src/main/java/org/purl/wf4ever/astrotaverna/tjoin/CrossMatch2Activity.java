package org.purl.wf4ever.astrotaverna.tjoin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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


public class CrossMatch2Activity extends
		AbstractAsynchronousActivity<CrossMatch2ActivityConfigurationBean>
		implements AsynchronousActivity<CrossMatch2ActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final String IN_FIRST_TABLE = "VOTable1";
	private static final String IN_SECOND_TABLE = "VOTable2";
	private static final String IN_MATCHER = "matcher";
	private static final String IN_PARAMS = "params";
	private static final String IN_VALUES_1 = "values1";
	private static final String IN_VALUES_2 = "values2";
	private static final String IN_TUNING = "tuning";
	private static final String IN_SCORECOL = "scoreCol";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	
	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private CrossMatch2ActivityConfigurationBean configBean;

	
	private static Logger logger = Logger.getLogger(CrossMatch2Activity.class);
	
	@Override
	public void configure(CrossMatch2ActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		//if (!configBean.getTablefile1().exists()) {
		//	throw new ActivityConfigurationException(
		//			"Input table file 1 doesn't exist");
		//}
		
		if(!(      configBean.getTypeOfInput().compareTo("File")==0
				|| configBean.getTypeOfInput().compareTo("URL")==0
				|| configBean.getTypeOfInput().compareTo("String")==0)){
			throw new ActivityConfigurationException(
					"Invalid input type for the tables");
		}
		
		if(!(configBean.getFixcols().compareTo("none")==0
				|| configBean.getFixcols().compareTo("dups")==0
				|| configBean.getFixcols().compareTo("all")==0)){
			throw new ActivityConfigurationException(
					"Invalid fixcols parameter.");
		}
		
		if(!(configBean.getJoin().compareTo("1and2")==0
				|| configBean.getJoin().compareTo("1or2")==0
				|| configBean.getJoin().compareTo("all1")==0
				|| configBean.getJoin().compareTo("all2")==0
				|| configBean.getJoin().compareTo("1not2")==0
				|| configBean.getJoin().compareTo("2not1")==0
				|| configBean.getJoin().compareTo("1xor2")==0)){
			throw new ActivityConfigurationException(
					"Invalid join parameter.");
		}
		
		if(!(configBean.getMatchCriteria().compareTo("sky")==0
				|| configBean.getMatchCriteria().compareTo("skyerr")==0
				|| configBean.getMatchCriteria().compareTo("skyellipse")==0
				|| configBean.getMatchCriteria().compareTo("sky3d")==0
				|| configBean.getMatchCriteria().compareTo("exact")==0
				|| configBean.getMatchCriteria().compareTo("1d")==0
				|| configBean.getMatchCriteria().compareTo("2d")==0
				|| configBean.getMatchCriteria().compareTo("2d_anisotropic")==0
				|| configBean.getMatchCriteria().compareTo("1d_err")==0
				|| configBean.getMatchCriteria().compareTo("2d_err")==0
				|| configBean.getMatchCriteria().compareTo("2d_ellipse")==0
				|| configBean.getMatchCriteria().compareTo("other")==0)){
			throw new ActivityConfigurationException(
					"Invalid matcher parameter.");
		}
		
		
		if(!(configBean.getFind().compareTo("all")==0
				|| configBean.getFind().compareTo("best")==0
				|| configBean.getFind().compareTo("best1")==0
				|| configBean.getFind().compareTo("best2")==0)){
			throw new ActivityConfigurationException(
					"Invalid find parameter.");
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
		addInput(IN_FIRST_TABLE, 0, true, null, String.class);
		addInput(IN_SECOND_TABLE, 0, true, null, String.class);
		
		//File name for the output table
		if(configBean.getTypeOfInput().compareTo("File")==0){
			addInput(IN_OUTPUT_TABLE_NAME, 0, true, null, String.class);
		}
		
		if(configBean.getMatchCriteria().compareTo("other")==0){
			addInput(IN_MATCHER, 0, true, null, String.class);
		}
		
		addInput(IN_VALUES_1, 0, true, null, String.class);
		addInput(IN_VALUES_2, 0, true, null, String.class);
		
		if(configBean.getMatchCriteria().compareTo("exact")!=0){
			addInput(IN_TUNING, 0, true, null, String.class); //Not mandatory
			addInput(IN_PARAMS, 0, true, null, String.class); //Not mandatory
		}
		
		if(configBean.getShowScoreCol()){
			addInput(IN_SCORECOL, 0, true, null, String.class);
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
					if(inputs.get(IN_FIRST_TABLE)==null)
						validStatus = false;
					else if(inputs.get(IN_SECOND_TABLE)==null)
						validStatus = false;
					else if(inputs.get(IN_VALUES_1)==null)
						validStatus = false;
					else if(inputs.get(IN_VALUES_2)==null)
						validStatus = false;
					else if(configBean.getTypeOfInput().compareTo("File")==0 
							&& inputs.get(IN_OUTPUT_TABLE_NAME)==null)
						validStatus = false;
					else if(configBean.getMatchCriteria().compareTo("other")==0 && inputs.get(IN_MATCHER)==null)
						validStatus = false;
					else if(configBean.getShowScoreCol() && inputs.get(IN_SCORECOL)==null)
						validStatus = false;
					
					//PARAM  and TUNING are not mandatories
					
					//if(configBean.getMatchCriteria().compareTo("exact")!=0 && inputs.get(IN_TUNING)==null)
					//	validStatus = false;
					if(configBean.getMatchCriteria().compareTo("exact")!=0 && inputs.get(IN_PARAMS)==null)
						validStatus = false;
					
				}catch(Exception ex){validStatus = false;}
				
				return validStatus;
			}
			
			public void run() {
				boolean callbackfails=false;
				//File tmpInFile1 = null;
				//File tmpInFile2 = null;
				List<File> tmpFiles = new ArrayList();
				String votable1, votable2;
				String params = "";
				String tuning = "";
				String values1, values2;
				String combinedMatcher = "";
				
				File tmpOutFile = null;
				int numTables;
				int iref = 1;
				boolean othermatcher = false;
				boolean exactmatcher = false;
				boolean scorecolispresent = false;
				String scoreCol = "";
				
				
				if(areMandatoryInputsNotNull()){
				
					InvocationContext context = callback
							.getContext();
					ReferenceService referenceService = context
							.getReferenceService();
					
					// RESOLVE INPUTS			
					votable1 = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_TABLE), 
							String.class, context);
					votable2 = (String) referenceService.renderIdentifier(inputs.get(IN_SECOND_TABLE), 
							String.class, context);
					values1 = (String) referenceService.renderIdentifier(inputs.get(IN_VALUES_1), 
							String.class, context);
					values2 = (String) referenceService.renderIdentifier(inputs.get(IN_VALUES_2), 
							String.class, context);
					
					if(configBean.getMatchCriteria().compareTo("other")==0){
						othermatcher = true;
						combinedMatcher = (String) referenceService.renderIdentifier(inputs.get(IN_MATCHER), 
								String.class, context);
					}
					
					if(configBean.getMatchCriteria().compareTo("exact")!=0){
						
						
						if(inputs.get(IN_PARAMS)!=null)
							params = (String) referenceService.renderIdentifier(inputs.get(IN_PARAMS), 
									String.class, context);
						if(params==null)
							params ="";
						if(inputs.get(IN_TUNING)!=null)
							tuning = (String) referenceService.renderIdentifier(inputs.get(IN_TUNING), 
									String.class, context);
						if(tuning == null )
							tuning = "";
					}else{
						exactmatcher = true;
					}
					
					if(configBean.getShowScoreCol()){
						scorecolispresent = true;
						scoreCol =  (String) referenceService.renderIdentifier(inputs.get(IN_SCORECOL), 
								String.class, context);
					}
					
					//SET UP RUNNING ENVIRONMENT: creating temporary files, ...
					
					
					boolean optionalFilePorts = configBean.getTypeOfInput().compareTo("File")==0;
					
					String outputTableName = null;
					if(optionalFilePorts && inputs.containsKey(IN_OUTPUT_TABLE_NAME)){ //configBean.getNumberOfTables()==3
						outputTableName = (String) referenceService.renderIdentifier(inputs.get(IN_OUTPUT_TABLE_NAME), 
								String.class, context);
					}
	
					
					tmpFiles = new ArrayList();
					ArrayList<String> tables = new ArrayList<String>();
					tables.add(votable1);
					tables.add(votable2);
					
					if(configBean.getTypeOfInput().compareTo("File")==0){
						for(int i=0; i< tables.size();i++){
							File file = new File(tables.get(i));
							tmpFiles.add(file);
							
							if(!file.exists()){
								callback.fail("Input table file does not exist: "+ tables,new IOException());
								callbackfails = true;
							}
						}
					}
					
					
					if(configBean.getTypeOfInput().compareTo("URL")==0){
						try {
							for(String testUrl: tables){
								URI exampleUri = new URI(testUrl);
							}
						} catch (URISyntaxException e) {
							callback.fail("Invalid URL: "+ tables,e);
							callbackfails = true;
						}
					}
					
					
					//prepare tmp input files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0){
						try{
							for(int i = 0; i<tables.size(); i++){
								File file = MyUtils.writeStringAsTmpFile(tables.get(i));
								file.deleteOnExit();
								tables.set(i, file.getAbsolutePath());
							}
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
							callbackfails = true;
						}
					}
					
					//prepare tmp output files if needed
					if(configBean.getTypeOfInput().compareTo("String")==0
							|| configBean.getTypeOfInput().compareTo("URL")==0){
						try{
							tmpOutFile = File.createTempFile("astro", null);
							tmpOutFile.deleteOnExit();
							outputTableName = tmpOutFile.getAbsolutePath();
						}catch(Exception ex){
							callback.fail("It wasn't possible to create a temporary file",ex);
							callbackfails = true;
						}
					}
					
					
					
					
					numTables = tables.size();
					
				
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
					ArrayList<String> parametersList = new ArrayList<String>();
					
					if(!callbackfails){
						int currentParam=0;
						int paramNumber = 0;
						/*
						   stilts <stilts-flags> tmatch2 ifmt1=<in-format> ifmt2=<in-format>
                                 icmd1=<cmds> icmd2=<cmds> ocmd=<cmds>
                                 omode=<out-mode> <mode-args> out=<out-table>
                                 ofmt=<out-format> matcher=<matcher-name>
                                 values1=<expr-list> values2=<expr-list>
                                 params=<match-params> tuning=<tuning-params>
                                 join=1and2|1or2|all1|all2|1not2|2not1|1xor2
                                 find=all|best|best1|best2
                                 fixcols=none|dups|all suffix1=<label>
                                 suffix2=<label> scorecol=<col-name>
                                 progress=none|log|profile
                                 [in1=]<table1> [in2=]<table2>
                        */

						
						//set up parameters 
						parametersList.add("tmatch2");
						parametersList.add("in1="+tables.get(0));
						parametersList.add("in2="+tables.get(1));
						
						parametersList.add("values2="+values2);
						parametersList.add("values1="+values1);
						
						//input format is not fixed --> auto mode
						//parameters[currentParam] = "omode=out"; currentParam++; default is out
						parametersList.add("out="+outputTableName);
						parametersList.add("ofmt=votable");
						
						if(!othermatcher)
							parametersList.add("matcher="+configBean.getMatchCriteria());
						else
							parametersList.add("matcher="+combinedMatcher);
						
						
						
						
						if(!exactmatcher){
							if(inputs.get(IN_PARAMS)!=null)
								parametersList.add("params='"+params+"'");
							if(inputs.get(IN_TUNING)!=null)
								parametersList.add("tuning='"+tuning+"'");
						}
						
						parametersList.add("join="+configBean.getJoin());
						parametersList.add("find="+configBean.getFind());
						parametersList.add("fixcols="+configBean.getFixcols());
						if(scorecolispresent)
							parametersList.add("scorecol="+scoreCol);
						
						//Object [] objects = parametersList.toArray();
						//parameters = (String []) parametersList.toArray();
		
						parameters = new String[parametersList.size()];
						parametersList.toArray(parameters);
						
						SecurityManager securityBackup = System.getSecurityManager();
						System.setSecurityManager(new NoExitSecurityManager());
						
						try{
							System.setProperty("votable.strict", "false");
							//System.out.println(MyUtils.toSpacedString(parameters));
							Stilts.main(parameters);
						}catch(SecurityException ex){
							callback.fail("Invalid service call: check the input parameters", ex);
							callbackfails = true;
						}
					
						System.setSecurityManager(securityBackup);
						
					
						//iF()
						
						if(!callbackfails){
							// Register outputs
							Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
							String resultValue = "";// //Name of the output file or result
							String reportOutput = MyUtils.toSpacedString(parameters);
							
							if(optionalFilePorts){ //case File
								resultValue = outputTableName;
							}else if(configBean.getTypeOfInput().compareTo("URL")==0
										|| configBean.getTypeOfInput().compareTo("String")==0){
						
								try{
									resultValue = MyUtils.readFileAsString(tmpOutFile.getAbsolutePath());
								}catch (Exception ex){
									callback.fail("It wasn't possible to read the result from a temporary file", ex);
									callbackfails = true;
								}
							}
							if(!callbackfails){
								T2Reference simpleRef = referenceService.register(resultValue, 0, true, context);
								outputs.put(OUT_SIMPLE_OUTPUT, simpleRef);
								T2Reference simpleRef2 = referenceService.register(reportOutput,0, true, context); 
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
	public CrossMatch2ActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
