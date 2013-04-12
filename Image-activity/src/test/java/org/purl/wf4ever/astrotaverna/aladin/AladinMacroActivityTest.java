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
	//this test fails because the process that runs the script calls System.exit(1). I doesn't understand
	//this method fails and not the executeInvoker(). is this a thread issue?
	//TODO
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		configBean.setTypeOfMode("nogui");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		
		//inputs.put(FIRST_INPUT, "file:///Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script.ajs");
		//inputs.put(SECOND_INPUT, "file:///Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt");
		inputs.put(FIRST_INPUT, "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script.ajs");
		inputs.put(SECOND_INPUT, "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt");
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_STD_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_ERROR, String.class);
		expectedOutputTypes.put(VO_TABLE, String.class);
		Map<String, Object> outputs = new HashMap<String, Object>();
		try{
			outputs = ActivityInvoker.invokeAsyncActivity(
					activity, inputs, expectedOutputTypes);
		}catch(SecurityException ex){
			System.out.println("ERRORRR- invoking the activity at the test.");
		}

		assertEquals("Unexpected outputs", outputs.size(), 3);
		assertEquals("", outputs.get(OUT_STD_OUTPUT));
		assertEquals("", outputs.get(OUT_ERROR));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	//The process returns internaly 1 in a System.exit(), but it doesn't fail
	@Ignore
	@Test
	public void executeInvoker() throws Exception {
		
		AladinInvoker invoker = new AladinInvoker ();
		try{
			invoker.runMacro("/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script.ajs", "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt", "nogui");
		}catch(SecurityException ex){
			System.out.println("ERRORRR- invoking the activity at the test.");
		}

		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	//It is based in local files
	//This test should finish without any error
	@Ignore
	@Test
	public void executeAsynch_with_simple_script() throws Exception {
		configBean.setTypeOfInput("File");
		configBean.setTypeOfMode("nogui");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();

		inputs.put(FIRST_INPUT, "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script_short.ajs");
		inputs.put(SECOND_INPUT, "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt");
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_STD_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_ERROR, String.class);
		expectedOutputTypes.put(VO_TABLE, String.class);
		Map<String, Object> outputs = new HashMap<String, Object>();
		try{
			outputs = ActivityInvoker.invokeAsyncActivity(
					activity, inputs, expectedOutputTypes);
		}catch(SecurityException ex){
			System.out.println("ERRORRR- invoking the activity at the test.");
		}

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
