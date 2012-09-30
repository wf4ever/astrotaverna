package org.purl.wf4ever.astrotaverna.pdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

public class ValidationPDLClientActivityTest {

	private ValidationPDLClientActivityConfigurationBean configBean;

	//these variables must be the same than the ones defined in the activity class
	private static final String IN_FIRST_INPUT = "votable1";
	private static final String IN_SECOND_INPUT = "votable2";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	private static final String OUT_SIMPLE_OUTPUT = "outputFileOut";
	private static final String OUT_REPORT = "report";
	
	private ValidationPDLClientActivity activity = new ValidationPDLClientActivity();

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
	@Ignore
	@Before
	public void makeConfigBean() throws Exception {
		configBean = new ValidationPDLClientActivityConfigurationBean();
		
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");

	}

	@Ignore
	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		ValidationPDLClientActivityConfigurationBean invalidBean = new ValidationPDLClientActivityConfigurationBean();
		invalidBean.setPdlDescriptionFile("/home/PDL-Description.xml");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	

	//this test is valid only with the right folders

	
	@Test
	public void executeAsynch() throws Exception {
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		Float value = new Float(1/15.0);
		inputs.put("Ne", value.toString());
		inputs.put("Si", value.toString());
		inputs.put("Mg", value.toString());
		inputs.put("Cr", value.toString());
		inputs.put("Na", value.toString());
		inputs.put("Ar", value.toString());
		inputs.put("Al", value.toString());
		inputs.put("Ca", value.toString());
		inputs.put("Fe", value.toString());
		inputs.put("C", value.toString());
		inputs.put("N", value.toString());
		inputs.put("S", value.toString());
		inputs.put("Mn", value.toString());
		inputs.put("O", value.toString());
		inputs.put("Ni", value.toString());
		inputs.put("email", "email@iaa.es");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("simpleValue", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	@Ignore
	@Test(expected = Exception.class)
	public void executeAsynchWithUnexistingFile() throws Exception {
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT, "/home/julian/Documentos/wf4ever/tables/filenoexist.xml");
		inputs.put(IN_SECOND_INPUT, "/home/julian/Documentos/wf4ever/tables/filenoexist2.xml");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documentos/wf4ever/tables/file.xml");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Ignore
	@Test
	public void executeAsynchWitStrings() throws Exception {
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
	//	inputs.put(IN_FIRST_INPUT, table1);
	//	inputs.put(IN_SECOND_INPUT, table2);
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		//result = result.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		//tableresult = tableresult.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");		
		//assertTrue("Wrong output : ", (result.length()> tableresult.length()-6) && (result.length()< tableresult.length()+6));
		assertTrue("Wrong output : ", result.indexOf("nrows=\"3\"")!=-1);
		assertEquals("Unexpected outputs", 2, outputs.size());
		//assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	@Ignore
	@Test(expected = Exception.class)
	public void executeAsynchWitNullInport() throws Exception {
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		//inputs.put(IN_FIRST_INPUT, table1);
		//inputs.put(IN_SECOND_INPUT, table2);
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		assertTrue("Wrong output : ", result.indexOf("nrows=\"3\"")!=-1);
		assertEquals("Unexpected outputs", 2, outputs.size());
		//assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Ignore
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
		//System.out.println(configBean.getPdlDescriptionFile());
		activity.configure(configBean);
		assertEquals("Unexpected inputs", 16, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());

		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 16, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
		Iterator<ActivityInputPort> it = activity.getInputPorts().iterator();
		//while(it.hasNext()){
		//	ActivityInputPort act = it.next();
		//	//System.out.println(act.getName()+" depth: " + act.getDepth());
		//	System.out.println(act.getName());
		//}
	}

	@Ignore
	@Test
	public void reConfiguredPorts() throws Exception {
		activity.configure(configBean);

		ValidationPDLClientActivityConfigurationBean specialBean = new ValidationPDLClientActivityConfigurationBean();
		configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}
	
	
}
