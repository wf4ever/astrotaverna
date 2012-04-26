package com.astrotaverna.coordinatestool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

public class CoordinatesActivity extends
		AbstractAsynchronousActivity<CoordinatesActivityConfigurationBean>
		implements AsynchronousActivity<CoordinatesActivityConfigurationBean> {

	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
			/*
			 * Best practice: Keep port names as constants to avoid misspelling. This
			 * would not apply if port names are looked up dynamically from the service
			 * operation, like done for WSDL services.
			 */
			
			private static final String RA = "RA";
			private static final String DEC = "DEC";
			private static final String L_COORD = "L_COORD";
			private static final String B_COORD = "B_COORD";
	
	private CoordinatesActivityConfigurationBean configBean;

	@Override
	public void configure(CoordinatesActivityConfigurationBean configBean)
			throws ActivityConfigurationException {

		// Any pre-config sanity checks
		if (configBean.getExampleString().equals("invalidExample")) {
			throw new ActivityConfigurationException(
					"Example string can't be 'invalidExample'");
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

				
				addInput(RA, 0, true, null, String.class);
				addInput(DEC, 0, true, null, String.class);		
				addOutput(L_COORD, 0);
				addOutput(B_COORD, 0);

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
				String raInput = (String) referenceService.renderIdentifier(inputs.get(RA), 
						String.class, context);
				String decInput = (String) referenceService.renderIdentifier(inputs.get(DEC), 
						String.class, context);
				
				
				Double stheta = 0.88998808748;
				Double ctheta = 0.45598377618;
				Double psi = 0.57477043300;
				Double phi = 4.9368292465;
				
				Double ra = Double.parseDouble(raInput);
				Double dec = Double.parseDouble(decInput);
				Double raa = ra*Math.PI/180.0;
				Double deca = dec*Math.PI/180.0;
				
				Double a = raa - phi;
				Double b1 = deca;
				Double sb = Math.sin(b1);
				Double cb = Math.cos(b1);
				Double cbsa = cb * Math.sin(a);
				Double b = -stheta * cbsa + ctheta * sb;
				Double lout = Math.atan2(ctheta * cbsa + stheta * sb, cb * Math.cos(a))+psi;
				Double bout = Math.asin(b);
					    
				while(lout < 0.0){
				        lout = lout + 2.0*Math.PI;
				};
				while(lout >= 2.0*Math.PI){
				        lout = lout - 2.0*Math.PI;
				};
				Double l_coord = lout/Math.PI*180.0;
				Double b_coord = bout/Math.PI*180.0;
				
				
				// Register outputs
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				
				T2Reference lRef = referenceService.register(l_coord, 0, true, context);
				outputs.put(L_COORD, lRef);
				
				T2Reference bRef = referenceService.register(b_coord, 0, true, context);
				outputs.put(B_COORD, bRef);
				
			
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public CoordinatesActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
