package org.purl.wf4ever.astrotaverna.tpipe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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

	



	@Test
	public void configureActivity() throws Exception {
		Set<String> expectedInputs = new HashSet<String>();
		/*
		configBean.setNumberOfTables(3);
		
		expectedInputs.add("firstFile");
		expectedInputs.add("secondFile");
		if(configBean.getNumberOfTables()>2)
			expectedInputs.add("thirdFile");
		if(configBean.getNumberOfTables()>3)
			expectedInputs.add("fourthFile");
		expectedInputs.add("outputFileIn");
		
		
		Set<String> expectedOutputs = new HashSet<String>();
		expectedOutputs.add("outputFileOut");
		expectedOutputs.add("report");

		activity.configure(configBean);

		Set<ActivityInputPort> inputPorts = activity.getInputPorts();
		assertEquals(expectedInputs.size(), inputPorts.size());
		for (ActivityInputPort inputPort : inputPorts) {
			assertTrue("Wrong input : " + inputPort.getName(), expectedInputs
					.remove(inputPort.getName()));
		}

		Set<OutputPort> outputPorts = activity.getOutputPorts();
		assertEquals(expectedOutputs.size(), outputPorts.size());
		for (OutputPort outputPort : outputPorts) {
			assertTrue("Wrong output : " + outputPort.getName(),
					expectedOutputs.remove(outputPort.getName()));
		}
		*/
	}
	
	
}
