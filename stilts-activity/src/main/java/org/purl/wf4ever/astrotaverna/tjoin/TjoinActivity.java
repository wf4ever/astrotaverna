package org.purl.wf4ever.astrotaverna.tjoin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.starlink.ttools.Stilts;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class TjoinActivity extends
		AbstractAsynchronousActivity<TjoinActivityConfigurationBean>
		implements AsynchronousActivity<TjoinActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	private static final int MAX_IN_PORT_NUMBER = 4; 
	private static final String IN_FIRST_INPUT = "firstFile";
	private static final String IN_SECOND_INPUT = "secondFile";
	private static final String IN_THIRD_INPUT = "thirdFile";
	private static final String IN_FOURTH_INPUT = "fourthFile";
	private static final String IN_LAST_INPUT = "outputFileIn";
	//private static final String IN_EXTRA_DATA = "extraData";
	//private static final String OUT_MORE_OUTPUTS = "moreOutputs";
	private static final String OUT_SIMPLE_OUTPUT = "outputFileOut";
	private static final String OUT_REPORT = "report";
	
	private TjoinActivityConfigurationBean configBean;

	@Override
	public void configure(TjoinActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		//if (!configBean.getTablefile1().exists()) {
		//	throw new ActivityConfigurationException(
		//			"Input table file 1 doesn't exist");
		//}
		
		if(!(configBean.getInputFormat().compareTo("fits")==0 
				|| configBean.getInputFormat().compareTo("colfits")==0
				|| configBean.getInputFormat().compareTo("votable")==0
				|| configBean.getInputFormat().compareTo("ascii")==0
				|| configBean.getInputFormat().compareTo("csv")==0
				|| configBean.getInputFormat().compareTo("tst")==0
				|| configBean.getInputFormat().compareTo("ipac")==0)){
			throw new ActivityConfigurationException(
					"Invalid format for the input tables");
		}
		
		if(configBean.getNumberOfTables()<2 || configBean.getNumberOfTables()>MAX_IN_PORT_NUMBER){
			throw new ActivityConfigurationException(
					"Invalid number of input tables. Number beetwen 2 and "+MAX_IN_PORT_NUMBER);
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
		if(configBean.getNumberOfTables()==3){
			addInput(IN_THIRD_INPUT, 0, true, null, String.class);
		}else if(configBean.getNumberOfTables()==4){
			addInput(IN_THIRD_INPUT, 0, true, null, String.class);
			addInput(IN_FOURTH_INPUT, 0, true, null, String.class);
		}
		//File name for the output table
		addInput(IN_LAST_INPUT, 0, true, null, String.class);

		// Optional ports depending on configuration
		//if (configBean.getExampleString().equals("specialCase")) {
		//	// depth 1, ie. list of binary byte[] arrays
		//	addInput(IN_EXTRA_DATA, 1, true, null, byte[].class);
		//	addOutput(OUT_REPORT, 0);
		//}
		
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
				InvocationContext context = callback
						.getContext();
				ReferenceService referenceService = context
						.getReferenceService();
				// Resolve inputs 				
				String firstInput = (String) referenceService.renderIdentifier(inputs.get(IN_FIRST_INPUT), 
						String.class, context);
				String secondInput = (String) referenceService.renderIdentifier(inputs.get(IN_SECOND_INPUT), 
						String.class, context);
				
				String lastInput = (String) referenceService.renderIdentifier(inputs.get(IN_LAST_INPUT), 
						String.class, context);

				boolean optionalPorts = configBean.getNumberOfTables() > 2 && configBean.getNumberOfTables()<=MAX_IN_PORT_NUMBER;
				
				String thirdInput = null;
				String fourthInput = null;
				if(optionalPorts && inputs.containsKey(IN_THIRD_INPUT)){ //configBean.getNumberOfTables()==3
					thirdInput = (String) referenceService.renderIdentifier(inputs.get(IN_THIRD_INPUT), 
							String.class, context);
				}
				if(optionalPorts && inputs.containsKey(IN_FOURTH_INPUT)){//configBean.getNumberOfTables()==4
					thirdInput = (String) referenceService.renderIdentifier(inputs.get(IN_THIRD_INPUT), 
							String.class, context);
					fourthInput = (String) referenceService.renderIdentifier(inputs.get(IN_FOURTH_INPUT), 
							String.class, context);
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
				
				//set up parameters depending on the number of inputs
				if(thirdInput==null && fourthInput ==null){
					parameters = new String[7];
					parameters[0] = "tjoin";
					parameters[1] = "nin=2";
					parameters[2] = "in1="+firstInput;
					parameters[3] = "in2="+secondInput;
					parameters[4] = "out="+lastInput;
					parameters[5] = "ifmt1="+configBean.getInputFormat();
					parameters[6] = "ifmt2="+configBean.getInputFormat();
					
					Stilts.main(parameters);
					
				}else if(thirdInput!=null && fourthInput ==null){
					parameters = new String[9];
					parameters[0] = "tjoin";
					parameters[1] = "nin=2";
					parameters[2] = "in1="+firstInput;
					parameters[3] = "in2="+secondInput;
					parameters[4] = "in3="+thirdInput;
					parameters[5] = "out="+lastInput;
					parameters[6] = "ifmt1="+configBean.getInputFormat();
					parameters[7] = "ifmt2="+configBean.getInputFormat();
					parameters[8] = "ifmt3="+configBean.getInputFormat();
					
					Stilts.main(parameters);
					
				}else if(thirdInput!=null && fourthInput !=null){
					parameters = new String[11];
					parameters[0] = "tjoin";
					parameters[1] = "nin=2";
					parameters[2] = "in1="+firstInput;
					parameters[3] = "in2="+secondInput;
					parameters[4] = "in3="+thirdInput;
					parameters[5] = "in4="+fourthInput;
					parameters[6] = "out="+lastInput;
					parameters[7] = "ifmt1="+configBean.getInputFormat();
					parameters[8] = "ifmt2="+configBean.getInputFormat();
					parameters[9] = "ifmt3="+configBean.getInputFormat();
					parameters[10] = "ifmt4="+configBean.getInputFormat();
					
					Stilts.main(parameters);
					
				}

				// Register outputs
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				String simpleValue = lastInput;//Name of the output file
				String simpleoutput = "simple-report";
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
		});
	}

	@Override
	public TjoinActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
