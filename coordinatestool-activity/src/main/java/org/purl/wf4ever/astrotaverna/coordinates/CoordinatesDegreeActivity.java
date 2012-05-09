package org.purl.wf4ever.astrotaverna.coordinates;

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

public class CoordinatesDegreeActivity extends
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
			
			private static final String RA_HMS = "RA_HMS";
			private static final String DEC_DMS = "DEC_DMS";
			private static final String RA_DEG = "RA_DEG";
			private static final String DEC_DEG = "DEC_DEG";
	
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

				
				addInput(RA_HMS, 0, true, null, String.class);
				addInput(DEC_DMS, 0, true, null, String.class);		
				addOutput(RA_DEG, 0);
				addOutput(DEC_DEG, 0);

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
				String raInput = (String) referenceService.renderIdentifier(inputs.get(RA_HMS), 
						String.class, context);
				String decInput = (String) referenceService.renderIdentifier(inputs.get(DEC_DMS), 
						String.class, context);
				

				double sign = 1.0;
				Double rah, ram,ras, decd, decm, decs;
				Double hh;
				Double deg;
				
				String[] temp = raInput.split(":");
				
				try{
					 rah = Double.parseDouble(temp[0]);
					 ram = Double.parseDouble(temp[1]);
					 ras = Double.parseDouble(temp[2]);
				} catch (Exception e) {
					 rah = Double.NaN;
					 ram = Double.NaN;
					 ras = Double.NaN;
				}
				temp = decInput.split(":");
				
				try{
					 decd = Double.parseDouble(temp[0]);
					 decm = Double.parseDouble(temp[1]);
					 decs = Double.parseDouble(temp[2]);
				} catch (Exception e) {
					 decd = Double.NaN;
					 decm = Double.NaN;
					 decs = Double.NaN;
				}
				//Calculating ra degrees		
				if (rah < 0.0) {
					sign = -1.0;
					rah = 0.0 - rah;
				}
				if (ram < 0.0) {
					sign = -1.0;
					ram = 0.0 - ram;
				}
				if (ras < 0.0) {
					sign = -1.0;
					ras = 0.0 - ras;
				}
				hh = sign*(rah + ram/60.0 + ras/3600.0);
				
				deg = 15.0*hh;
				
				if (deg == 360.0)
					deg = 0.0;
				
				Double ra_deg = deg;
				
				//Calculating DEC degrees
				if (decd < 0.0) {
					sign = -1.0;
					decd = 0.0 - decd;
				}
				if (decm < 0.0) {
					sign = -1.0;
					decm = 0.0 - decm;
				}
				if (decs < 0.0) {
					sign = -1.0;
					decs = 0.0 - decs;
				}
				hh = sign*(decd + decm/60.0 + decs/3600.0);
				Double dec_deg = hh;
						
				
				
				
				// Register outputs
				Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();
				
				T2Reference lRef = referenceService.register(ra_deg, 0, true, context);
				outputs.put(RA_DEG, lRef);
				
				T2Reference bRef = referenceService.register(dec_deg, 0, true, context);
				outputs.put(DEC_DEG, bRef);
				
			
				callback.receiveResult(outputs, new int[0]);
			}
		});
	}

	@Override
	public CoordinatesActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

}
