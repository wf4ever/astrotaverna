package org.purl.wf4ever.astrotaverna.aladin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;


import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/** 
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class AladinMacroActivityTest {

	private AladinMacroActivityConfigurationBean configBean;
	
	private static final String FIRST_INPUT = "script";
	private static final String SECOND_INPUT = "parameters";

	private static final String OUT_STD_OUTPUT = "STD_OUTPUT";
	private static final String OUT_ERROR = "ERROR_OUTPUT";
	private static final String VO_TABLE = "VOTable";
	
	private AladinMacroActivity activity = new AladinMacroActivity();

	
	@Ignore("Not ready to run")
	@BeforeClass
	public static void createTableFiles(){
		//create files with votables
	}

	@Ignore("Not ready to run")
	@AfterClass
	public static void deleteTableFiles(){
		//delete files with votables
	}
	
	//this method is invoked before each test method
	@Before
	public void makeConfigBean() throws Exception {
		configBean = new AladinMacroActivityConfigurationBean();	
		configBean.setTypeOfInput("File");
		configBean.setTypeOfMode("nogui");
		
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		AladinMacroActivityConfigurationBean invalidBean = new AladinMacroActivityConfigurationBean();
		invalidBean.setTypeOfInput("another thing");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration2() throws ActivityConfigurationException {
		AladinMacroActivityConfigurationBean invalidBean = new AladinMacroActivityConfigurationBean();
		invalidBean.setTypeOfInput("vFile");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}

	//this test is valid only with the right folders
	
	
	//It is based in local files
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("URL");
		configBean.setTypeOfMode("nogui");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		
		inputs.put(FIRST_INPUT, "file:///Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_script.ajs");
		inputs.put(SECOND_INPUT, "file:///Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_params.txt");
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_STD_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_ERROR, String.class);
		expectedOutputTypes.put(VO_TABLE, String.class);
		
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", outputs.size(), 3);
		assertEquals("", outputs.get(OUT_STD_OUTPUT));
		assertEquals("", outputs.get(OUT_ERROR));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	//based on lacal files (on ubuntu)
	@Ignore
	@Test
	public void executeAsynchOnUbuntu() throws Exception {
		configBean.setTypeOfInput("URL");
		configBean.setTypeOfMode("nogui");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		
		inputs.put(FIRST_INPUT, "file:///home/julian/Documentos/wf4ever/aladin/Aladin_workflow_script.ajs");
		inputs.put(SECOND_INPUT, "file:///home/julian/Documentos/wf4ever/aladin/Aladin_workflow_params.txt");
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();

		expectedOutputTypes.put(OUT_STD_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_ERROR, String.class);
		expectedOutputTypes.put(VO_TABLE, String.class);
		
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", outputs.size(), 3);
		assertEquals("", outputs.get(OUT_STD_OUTPUT));
		assertEquals("", outputs.get(OUT_ERROR));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	
	

	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
	

		activity.configure(configBean);
	
		//System.out.print(activity.getInputPorts().size());
		//for (ActivityInputPort activ : activity.getInputPorts()){
		//	System.out.print(activ.getName()+", ");
		//}
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 3, activity.getOutputPorts().size());
	
		
		
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 3, activity.getOutputPorts().size());
	}

	
}
