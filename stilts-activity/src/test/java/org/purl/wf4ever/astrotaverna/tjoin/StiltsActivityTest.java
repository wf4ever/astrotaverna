package org.purl.wf4ever.astrotaverna.tjoin;

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

public class StiltsActivityTest {

	private TjoinActivityConfigurationBean configBean;

	private TjoinActivity activity = new TjoinActivity();

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
		configBean = new TjoinActivityConfigurationBean();
		
		configBean.setCmd("");
		configBean.setInputFormat("votable");
		configBean.setNumberOfTables(2);
		
		//configBean.setExampleString("something");
		//configBean.setExampleUri(URI.create("http://localhost:8080/myEndPoint"));
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		TjoinActivityConfigurationBean invalidBean = new TjoinActivityConfigurationBean();
		invalidBean.setInputFormat("vo_table");
		invalidBean.setNumberOfTables(2);
		invalidBean.setCmd("");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration2() throws ActivityConfigurationException {
		TjoinActivityConfigurationBean invalidBean = new TjoinActivityConfigurationBean();
		invalidBean.setNumberOfTables(1);
		invalidBean.setInputFormat("votable");
		invalidBean.setCmd("");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}

	//this test is valid only with the right folders
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setInputFormat("ascii");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("firstFile", "/home/julian/Documents/tables/table1.ascii");
		inputs.put("secondFile", "/home/julian/Documents/tables/table1.ascii");
		inputs.put("outputFileIn", "/home/julian/Documents/tables/combined_test.xml");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put("outputFileOut", String.class);
		expectedOutputTypes.put("report", String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("/home/julian/Documents/tables/combined_test.xml", outputs.get("outputFileOut"));
		assertEquals("simple-report", outputs.get("report"));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());

		activity.configure(configBean);
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());

		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
	@Test
	public void reConfiguredPorts() throws Exception {
		activity.configure(configBean);

		TjoinActivityConfigurationBean specialBean = new TjoinActivityConfigurationBean();
		specialBean.setCmd("");
		specialBean.setInputFormat("votable");
		specialBean.setNumberOfTables(3);

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}
	
	@Test
	public void reConfiguredNumberPorts() throws Exception {
		activity.configure(configBean);

		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
		
		TjoinActivityConfigurationBean specialBean = new TjoinActivityConfigurationBean();
		specialBean.setCmd("");
		specialBean.setInputFormat("votable");
		specialBean.setNumberOfTables(3);

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
		
		specialBean.setNumberOfTables(4);
		activity.configure(specialBean);
		
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}


	@Test
	public void configureActivity() throws Exception {
		Set<String> expectedInputs = new HashSet<String>();
		
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
	}
	
	
}
