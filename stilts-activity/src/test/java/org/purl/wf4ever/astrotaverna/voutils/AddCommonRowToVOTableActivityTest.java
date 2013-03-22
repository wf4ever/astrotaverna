package org.purl.wf4ever.astrotaverna.voutils;

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

public class AddCommonRowToVOTableActivityTest {

	private AddCommonRowToVOTableActivityConfigurationBean configBean;

	//these variables must be the same than the ones defined in the activity class
	
	private static final String IN_FIRST_INPUT = "commonRowVOTable";
	private static final String IN_SECOND_INPUT = "mainVOTable";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	
	private static final String OUT_SIMPLE_OUTPUT = "VOTable";
	private static final String OUT_REPORT = "report";
	
	private AddCommonRowToVOTableActivity activity = new AddCommonRowToVOTableActivity();

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
		configBean = new AddCommonRowToVOTableActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");

	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		AddCommonRowToVOTableActivityConfigurationBean invalidBean = new AddCommonRowToVOTableActivityConfigurationBean();
		invalidBean.setTypeOfInput("Fileon");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	

	
	
	@Test(expected = Exception.class)
	public void executeAsynchWithUnexistingFile() throws Exception {
		configBean.setTypeOfInput("File");
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

	@Test
	public void executeAsynchWitStrings() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT, this.shortTable);
		inputs.put(IN_SECOND_INPUT, this.mainTable);
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals("valid", outputs.get(OUT_REPORT));
		
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

		AddCommonRowToVOTableActivityConfigurationBean specialBean = new AddCommonRowToVOTableActivityConfigurationBean();
		specialBean.setTypeOfInput("String");

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 2, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}
	
	
	private static final String shortTable = 
			"<?xml version='1.0'?>"
					+ "<VOTABLE version=\"1.1\""
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
					+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
					+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
					+ "<RESOURCE>"
					+ "<TABLE name=\"astro1467408768841096458.tmp\" nrows=\"2\">" 
					+ "<PARAM datatype=\"float\" name=\"inputRA\" unit=\"degrees\" value=\"195.16333\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputDEC\" unit=\"degrees\" value=\"2.5007777\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputSR\" unit=\"degrees\" value=\"0.001\"/>"
					+ "<FIELD ID=\"U\" datatype=\"float\" name=\"U\" ucd=\"PHOT_SDSS_U FIT_PARAM\"/>"
					+ "<FIELD ID=\"G\" datatype=\"float\" name=\"G\" ucd=\"PHOT_SDSS_G FIT_PARAM\"/>"
					+ "<DATA>"
					+ "<TABLEDATA>"
					+ "  <TR>"
					+ "    <TD>10.52193</TD>"
					+ "    <TD>10.47281</TD>"
					+ "  </TR>"
					+ "  <TR>"
					+ "    <TD>13.7583</TD>"
					+ "    <TD>12.79937</TD>"
					+ "  </TR>"
					+ "</TABLEDATA>"
					+ "</DATA>"
					+ "</TABLE>"
					+ "</RESOURCE>"
					+ "</VOTABLE>";
	
	private static final String mainTable = 
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
