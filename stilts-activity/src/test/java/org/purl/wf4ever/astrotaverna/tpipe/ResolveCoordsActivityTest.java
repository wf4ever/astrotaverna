package org.purl.wf4ever.astrotaverna.tpipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.OutputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.purl.wf4ever.astrotaverna.utils.MyUtils;

/**
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class ResolveCoordsActivityTest {

	private ResolveCoordsActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at ResolveCoordsActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	private static final String IN_RA_COLUMN = "nameRA";
	private static final String IN_DEC_COLUMN = "nameDEC";
	private static final String IN_COL_ID_OBJ_NAME = "objectName";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private ResolveCoordsActivity activity = new ResolveCoordsActivity();

	
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
		configBean = new ResolveCoordsActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration2() throws ActivityConfigurationException {
		ResolveCoordsActivityConfigurationBean invalidBean = new ResolveCoordsActivityConfigurationBean();
		invalidBean.setTypeOfInput("vFile");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}

	//this test is valid only with the right folders
	
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, "/home/julian/Documents/wf4ever/tables/othershortvotable.xml");
		inputs.put(IN_RA_COLUMN, "raNew");
		inputs.put(IN_DEC_COLUMN, "decNew");
		inputs.put(IN_COL_ID_OBJ_NAME, "name");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.xml");

		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("/home/julian/Documents/wf4ever/tables/resultTable.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Test
	public void executeAsynchWithStrings() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOTable2());
		inputs.put(IN_RA_COLUMN, "raNew");
		inputs.put(IN_DEC_COLUMN, "decNew");
		inputs.put(IN_COL_ID_OBJ_NAME, "name");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		try{
			outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

			assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("raNew")!=-1);
			assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("decNew")!=-1);
		
		}catch(Exception ex){System.out.println(ex.toString());}
		
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithNullInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOTable2());
		inputs.put(IN_RA_COLUMN, "raNew");
		inputs.put(IN_COL_ID_OBJ_NAME, "name");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("raNew")!=-1);
		assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("decNew")!=-1);

	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithInvalidInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOTable2());
		inputs.put(IN_RA_COLUMN, "raNew");
		inputs.put(IN_DEC_COLUMN, "decNew");
		inputs.put(IN_COL_ID_OBJ_NAME, "thisisnotacolumn");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("raNew")!=-1);
		assertTrue("Wrong output: ", ((String)outputs.get(OUT_SIMPLE_OUTPUT)).indexOf("decNew")!=-1);
		
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
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	
		
		
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

}
	
