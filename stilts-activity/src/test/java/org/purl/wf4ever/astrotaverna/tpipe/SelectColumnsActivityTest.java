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
import org.purl.wf4ever.astrotaverna.voutils.TestUtils;
/**
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class SelectColumnsActivityTest {

	private SelectColumnsActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at SelectColumnsActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
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
	
	@Ignore
	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, "/home/julian/Documents/wf4ever/tables/sdss_votable2.xml");
		inputs.put(IN_FILTER, "U G R I Z");
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
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FILTER, "U G R I Z");
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectColumns.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
						
		/*
		System.out.println(a.length());
		System.out.println(b.length());
		int i;
		boolean iguales=true;
		for(i=330;i<1206 && iguales;i++)
			if(a.charAt(i)!=b.charAt(i)){
				iguales = false;
			}
		System.out.println(i);
		System.out.println(a.substring(i-10, i+10));
		System.out.println(b.substring(i-10, i+10));
		*/
                TestUtils.compareStringLengthsIgnoreWhiteSpace(a, b, 6);
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test
	public void executeAsynchWithUCDs() throws Exception {
		configBean.setTypeOfInput("String");
		configBean.setTypeOfFilter("UCDs");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FILTER, "PHOT_SDSS_U;FIT_PARAM PHOT_SDSS_G;FIT_PARAM; PHOT_SDSS_R;FIT_PARAM PHOT_SDSS_I;FIT_PARAM PHOT_SDSS_Z;FIT_PARAM");

		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		System.out.println("*****************************************************************");
		
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectColumns.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		System.out.println("*****************************************************************");
		System.out.println(b);
						
		/*
		System.out.println(a.length());
		System.out.println(b.length());
		int i;
		boolean iguales=true;
		for(i=330;i<1206 && iguales;i++)
			if(a.charAt(i)!=b.charAt(i)){
				iguales = false;
			}
		System.out.println(i);
		System.out.println(a.substring(i-10, i+10));
		System.out.println(b.substring(i-10, i+10));
		*/
		
                TestUtils.compareStringLengthsIgnoreWhiteSpace(a, b, 6);
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Test(expected = RuntimeException.class)
	public void executeAsynchWithNullInput() throws Exception {
		configBean.setTypeOfInput("String");
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		//inputs.put(IN_FILTER, null);
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
		
				
                TestUtils.compareStringLengthsIgnoreWhiteSpace(a, b, 6);
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		
	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithInvalidInput() throws Exception {
		configBean.setTypeOfInput("String");
		configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_FILTER, "sds");
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
		
				
                TestUtils.compareStringLengthsIgnoreWhiteSpace(a, b, 6);
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		
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

	
	private static final String resultSelectColumns = 
			"<?xml version='1.0'?>"
					+ "<VOTABLE version=\"1.1\""
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
					+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
					+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
					+ "<!--"
					+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
					+ " !  at 2012-05-18T09:05:51"
					+ " !-->"
					+ "<RESOURCE>"
					+ "<TABLE name=\"astro1467408768841096458.tmp\" nrows=\"2\">" 
					+ "<PARAM datatype=\"float\" name=\"inputRA\" unit=\"degrees\" value=\"195.16333\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputDEC\" unit=\"degrees\" value=\"2.5007777\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputSR\" unit=\"degrees\" value=\"0.001\"/>"
					+ "<PARAM arraysize=\"1\" datatype=\"char\" name=\"rowcount, table 0\" value=\"2\"/>"
					+ "<FIELD ID=\"U\" datatype=\"float\" name=\"U\" ucd=\"PHOT_SDSS_U;FIT_PARAM\"/>"
					+ "<FIELD ID=\"G\" datatype=\"float\" name=\"G\" ucd=\"PHOT_SDSS_G;FIT_PARAM\"/>"
					+ "<FIELD ID=\"R\" datatype=\"float\" name=\"R\" ucd=\"PHOT_SDSS_R;FIT_PARAM\"/>"
					+ "<FIELD ID=\"I\" datatype=\"float\" name=\"I\" ucd=\"PHOT_SDSS_I;FIT_PARAM\"/>"
					+ "<FIELD ID=\"Z\" datatype=\"float\" name=\"Z\" ucd=\"PHOT_SDSS_Z;FIT_PARAM\"/>"
					+ "<DATA>"
					+ "<TABLEDATA>"
					+ "  <TR>"
					+ "    <TD>17.52193</TD>"
					+ "    <TD>17.47281</TD>"
					+ "    <TD>17.50826</TD>"
					+ "    <TD>17.99788</TD>"
					+ "    <TD>17.8128</TD>"
					+ "  </TR>"
					+ "  <TR>"
					+ "    <TD>13.7583</TD>"
					+ "    <TD>12.79937</TD>"
					+ "    <TD>12.27589</TD>"
					+ "    <TD>11.99715</TD>"
					+ "    <TD>11.71331</TD>"
					+ "  </TR>"
					+ "</TABLEDATA>"
					+ "</DATA>"
					+ "</TABLE>"
					+ "</RESOURCE>"
					+ "</VOTABLE>";
	
}
