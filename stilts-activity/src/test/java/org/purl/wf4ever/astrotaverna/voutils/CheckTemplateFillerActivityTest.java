package org.purl.wf4ever.astrotaverna.voutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.HashMap;

import java.util.Map;

import org.purl.wf4ever.astrotaverna.utils.*;

import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
//import org.purl.wf4ever.astrotaverna.utils.*;
//import org.purl.wf4ever.astrotaverna.utils.MyUtils;

//import org.purl.wf4ever.astrotaverna.voutils.CheckTemplateFillerActivityConfigurationBean;

/**
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class CheckTemplateFillerActivityTest {

	private CheckTemplateFillerActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at CheckTemplateFillerActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	private static final String IN_SECOND_INPUT = "template";
	private static final String IN_THIRD_INPUT = "vocabulary";

	private static final String OUT_REPORT = "report";
	
	private CheckTemplateFillerActivity activity = new CheckTemplateFillerActivity();

	
	
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
		configBean = new CheckTemplateFillerActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		
		//configBean.setExampleString("something");
		//configBean.setExampleUri(URI.create("http://localhost:8080/myEndPoint"));
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		CheckTemplateFillerActivityConfigurationBean invalidBean = new CheckTemplateFillerActivityConfigurationBean();
		invalidBean.setTypeOfInput("another thing");

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
		inputs.put(IN_FIRST_INPUT_TABLE, "/home/julian/Documents/wf4ever/showcase61.2goldenExampler/2GE/workflows/cat_sex_total_noback.votable");
		inputs.put(IN_SECOND_INPUT, "/home/julian/Documents/wf4ever/showcase61.2goldenExampler/2GE/workflows/galfit_template.txt");
		inputs.put(IN_THIRD_INPUT, "/home/julian/Documents/wf4ever/showcase61.2goldenExampler/2GE/workflows/galfit_vocabulary.txt");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		String report = "report ok";
		assertEquals("Unexpected outputs", 1, outputs.size());
		assertEquals("report ok", outputs.get(OUT_REPORT));
		assertEquals(report, outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test
	public void borrame() throws Exception {
		
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_SECOND_INPUT, "template text: $U$ $G$ $R$");
		inputs.put(IN_THIRD_INPUT, "U G R Z");
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		try{
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		assertEquals("report ok", outputs.get(OUT_REPORT));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	@Test
	public void executeAsynchString() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_SECOND_INPUT, "template text: $U$ $G$ $R$");
		inputs.put(IN_THIRD_INPUT, "U G R Z");
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		try{
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		assertEquals("report ok", outputs.get(OUT_REPORT));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithNullInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		
		
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		
	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithInvalidInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_SECOND_INPUT, "template text: $U$ $G$ $R$ ");
		inputs.put(IN_THIRD_INPUT, "U G");
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
	}
	
	@Test(expected = RuntimeException.class)
	public void executeAsynchWithInvalidInput2() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		inputs.put(IN_SECOND_INPUT, "template text: $U$ $G$ $R$ $thisisnotinvotable$");
		inputs.put(IN_THIRD_INPUT, "U G R");
		
		
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
	}
	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
	

		activity.configure(configBean);
	
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 1, activity.getOutputPorts().size());
	
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 1, activity.getOutputPorts().size());
	}

	
	
	private static final String resultCheckTemplateFiller = 
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
					+ "<FIELD ID=\"U\" datatype=\"float\" name=\"U\" ucd=\"PHOT_SDSS_U FIT_PARAM\"/>"
					+ "<FIELD ID=\"G\" datatype=\"float\" name=\"G\" ucd=\"PHOT_SDSS_G FIT_PARAM\"/>"
					+ "<FIELD ID=\"R\" datatype=\"float\" name=\"R\" ucd=\"PHOT_SDSS_R FIT_PARAM\"/>"
					+ "<FIELD ID=\"I\" datatype=\"float\" name=\"I\" ucd=\"PHOT_SDSS_I FIT_PARAM\"/>"
					+ "<FIELD ID=\"Z\" datatype=\"float\" name=\"Z\" ucd=\"PHOT_SDSS_Z FIT_PARAM\"/>"
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
