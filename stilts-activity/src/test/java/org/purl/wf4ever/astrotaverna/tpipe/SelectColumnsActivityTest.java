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

public class SelectColumnsActivityTest {

	private SelectColumnsActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at SelectColumnsActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "firstTable";
	private static final String IN_FORMAT_INPUT_TABLE = "formatTableIn";
	private static final String IN_FORMAT_OUTPUT_TABLE = "formatTableOut";
	private static final String IN_FILTER = "filter";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private SelectColumnsActivity activity = new SelectColumnsActivity();

	private static final String resultSelectColumns = 
			   "# U        G        R        I        Z         \n"       
			  +"  17.52193 17.47281 17.50826 17.99788 17.8128\n" 
			  +"  13.7583  12.79937 12.27589 11.99715 11.71331";
	
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
		configBean = new SelectColumnsActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		configBean.setTypeOfFilter("Column names");
		
		
		//configBean.setExampleString("something");
		//configBean.setExampleUri(URI.create("http://localhost:8080/myEndPoint"));
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		SelectColumnsActivityConfigurationBean invalidBean = new SelectColumnsActivityConfigurationBean();
		invalidBean.setTypeOfInput("another thing");
		invalidBean.setTypeOfFilter("Column names");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration2() throws ActivityConfigurationException {
		SelectColumnsActivityConfigurationBean invalidBean = new SelectColumnsActivityConfigurationBean();
		invalidBean.setTypeOfInput("vFile");
		invalidBean.setTypeOfFilter("ColumnNames");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}

	//this test is valid only with the right folders
	
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, "/home/julian/Documents/wf4ever/tables/sdss_votable2.xml");
		inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "U G R I Z");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("/home/julian/Documents/wf4ever/tables/resultTable.ascii", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Test
	public void executeAsynchWithStrings() throws Exception {
		configBean.setTypeOfInput("String");
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "U G R I Z");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectColumns.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		assertTrue("Wrong output : ", a.length()==b.length());
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
	

		activity.configure(configBean);
	
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
}
