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
 * @author Julian Garrido
 * @since    19 May 2011
 */
public class FormatConversionActivityTest {

	private FormatConversionActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at FormatConversionActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	private static final String IN_FORMAT_INPUT_TABLE = "formatTableIn";
	private static final String IN_FORMAT_OUTPUT_TABLE = "formatTableOut";
	//private static final String IN_FILTER = "filter";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private FormatConversionActivity activity = new FormatConversionActivity();

	private static final String resultFormatConversion = 
			   "# OBJID              RA               DEC              TYPE   U       G        R        I        Z        ERR_U       ERR_G       ERR_R       ERR_I       ERR_Z       PSFMAG_U PSFMAGERR_U PSFMAG_G PSFMAGERR_G PSFMAG_R PSFMAGERR_R PSFMAG_I PSFMAGERR_I PSFMAG_Z PSFMAGERR_Z \n"
			  +"  587726032792059952 195.162958157847 2.50144182633295 GALAXY 13.7583 12.79937 12.27589 11.99715 11.71331 0.004007341 0.001826325 0.001757175 0.001762709 0.002704954 17.67873 0.0193816   16.67595 0.03404235  16.40215 0.06255744  16.2727  0.02829609  16.17118 0.07506536  \n"
			  +"  587726032792059952 195.162958157847 2.50144182633295 GALAXY 13.7583  12.79937 12.27589 11.99715 11.71331 0.004007341 0.001826325 0.001757175 0.001762709 0.002704954 17.67873 0.0193816   16.67595 0.03404235  16.40215 0.06255744  16.2727  0.02829609  16.17118 0.07506536 \n";
	
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
		configBean = new FormatConversionActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		//configBean.setTypeOfFilter("Column names");
		
		
		//configBean.setExampleString("something");
		//configBean.setExampleUri(URI.create("http://localhost:8080/myEndPoint"));
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		FormatConversionActivityConfigurationBean invalidBean = new FormatConversionActivityConfigurationBean();
		invalidBean.setTypeOfInput("another thing");
		//invalidBean.setTypeOfFilter("Column names");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	//this test is valid only with the right folders
	
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, "/home/julian/Documents/wf4ever/tables/sdss_votable2.xml");
		inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		//inputs.put(IN_FILTER, "startsWith(TYPE,\"GALAXY\")");
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
	public void executeAsynchOtherFormats() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		//inputs.put(IN_FIRST_INPUT_TABLE, resultFormatConversion);
		//inputs.put(IN_FORMAT_INPUT_TABLE, "ascii");
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "votable-tabledata");

		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "csv");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		//String a = new String(resultFormatConversion.toCharArray());
		//String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		//assertTrue("Wrong output : ", (a.length()>b.length()-6) && (a.length()<b.length()+6));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		System.out.println((String)outputs.get(OUT_SIMPLE_OUTPUT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	
	@Test
	public void executeAsynchString() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		//inputs.put(IN_FILTER, "U < 15");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultFormatConversion.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		assertTrue("Wrong output : ", (a.length()>b.length()-6) && (a.length()<b.length()+6));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test
	public void executeAsynchWithDefaultFormats() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FORMAT_INPUT_TABLE, "");
		inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		//inputs.put(IN_FILTER, "U < 15");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultFormatConversion.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		assertTrue("Wrong output : ", (a.length()>b.length()-6) && (a.length()<b.length()+6));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
	

		activity.configure(configBean);
	
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
}
