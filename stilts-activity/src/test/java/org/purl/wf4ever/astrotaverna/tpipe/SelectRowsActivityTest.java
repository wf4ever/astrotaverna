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
public class SelectRowsActivityTest {

	private SelectRowsActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at SelectRowsActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	//private static final String IN_FORMAT_INPUT_TABLE = "formatTableIn";
	//private static final String IN_FORMAT_OUTPUT_TABLE = "formatTableOut";
	private static final String IN_FILTER = "filter";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private SelectRowsActivity activity = new SelectRowsActivity();

	private static final String resultSelectRowsASCII = 
			   "# OBJID              RA               DEC              TYPE   U       G        R        I        Z        ERR_U       ERR_G       ERR_R       ERR_I       ERR_Z       PSFMAG_U PSFMAGERR_U PSFMAG_G PSFMAGERR_G PSFMAG_R PSFMAGERR_R PSFMAG_I PSFMAGERR_I PSFMAG_Z PSFMAGERR_Z\n"
			  +"  587726032792059952 195.162958157847 2.50144182633295 GALAXY 13.7583 12.79937 12.27589 11.99715 11.71331 0.004007341 0.001826325 0.001757175 0.001762709 0.002704954 17.67873 0.0193816   16.67595 0.03404235  16.40215 0.06255744  16.2727  0.02829609  16.17118 0.07506536 \n";
	
	private static final String resultSelectRows = 
			   	  	"<?xml version='1.0'?>"
					+ "<VOTABLE version=\"1.1\""
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
					+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
					+ "xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
					+ "<!--"
					+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
					+ " !  at 2012-05-18T07:15:08"
					+ " !-->"
					+ "<RESOURCE>"
					+ "<TABLE name=\"astro8108859877890412455.tmp\">"  // name may vary at any execution
					+ "<PARAM datatype=\"float\" name=\"inputRA\" unit=\"degrees\" value=\"195.16333\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputDEC\" unit=\"degrees\" value=\"2.5007777\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputSR\" unit=\"degrees\" value=\"0.001\"/>"
					+ "<PARAM arraysize=\"1\" datatype=\"char\" name=\"rowcount, table 0\" value=\"2\"/>"
					+ "<FIELD ID=\"OBJID\" datatype=\"long\" name=\"OBJID\" ucd=\"ID_MAIN\">"
					+ "<VALUES null='-9223372036854775808'/>"
					+ "</FIELD>"
					+ "<FIELD ID=\"RA\" datatype=\"double\" name=\"RA\" ucd=\"POS_EQ_RA_MAIN\"/>"
					+ "<FIELD ID=\"DEC\" datatype=\"double\" name=\"DEC\" ucd=\"POS_EQ_DEC_MAIN\"/>"
					+ "<FIELD ID=\"TYPE\" arraysize=\"*\" datatype=\"char\" name=\"TYPE\" ucd=\"CLASS_OBJECT\"/>"
					+ "<FIELD ID=\"U\" datatype=\"float\" name=\"U\" ucd=\"PHOT_SDSS_U FIT_PARAM\"/>"
					+ "<FIELD ID=\"G\" datatype=\"float\" name=\"G\" ucd=\"PHOT_SDSS_G FIT_PARAM\"/>"
					+ "<FIELD ID=\"R\" datatype=\"float\" name=\"R\" ucd=\"PHOT_SDSS_R FIT_PARAM\"/>"
					+ "<FIELD ID=\"I\" datatype=\"float\" name=\"I\" ucd=\"PHOT_SDSS_I FIT_PARAM\"/>"
					+ "<FIELD ID=\"Z\" datatype=\"float\" name=\"Z\" ucd=\"PHOT_SDSS_Z FIT_PARAM\"/>"
					+ "<FIELD ID=\"ERR_U\" datatype=\"float\" name=\"ERR_U\" ucd=\"PHOT_SDSS_U ERROR\"/>"
					+ "<FIELD ID=\"ERR_G\" datatype=\"float\" name=\"ERR_G\" ucd=\"PHOT_SDSS_G ERROR\"/>"
					+ "<FIELD ID=\"ERR_R\" datatype=\"float\" name=\"ERR_R\" ucd=\"PHOT_SDSS_R ERROR\"/>"
					+ "<FIELD ID=\"ERR_I\" datatype=\"float\" name=\"ERR_I\" ucd=\"PHOT_SDSS_I ERROR\"/>"
					+ "<FIELD ID=\"ERR_Z\" datatype=\"float\" name=\"ERR_Z\" ucd=\"PHOT_SDSS_Z ERROR\"/>"
					+ "<FIELD ID=\"PSFMAG_U\" datatype=\"float\" name=\"PSFMAG_U\" ucd=\"PHOT_SDSS_U\"/>"
					+ "<FIELD ID=\"PSFMAGERR_U\" datatype=\"float\" name=\"PSFMAGERR_U\" ucd=\"PHOT_SDSS_U ERROR\"/>"
					+ "<FIELD ID=\"PSFMAG_G\" datatype=\"float\" name=\"PSFMAG_G\" ucd=\"PHOT_SDSS_G\"/>"
					+ "<FIELD ID=\"PSFMAGERR_G\" datatype=\"float\" name=\"PSFMAGERR_G\" ucd=\"PHOT_SDSS_G ERROR\"/>"
					+ "<FIELD ID=\"PSFMAG_R\" datatype=\"float\" name=\"PSFMAG_R\" ucd=\"PHOT_SDSS_R\"/>"
					+ "<FIELD ID=\"PSFMAGERR_R\" datatype=\"float\" name=\"PSFMAGERR_R\" ucd=\"PHOT_SDSS_R ERROR\"/>"
					+ "<FIELD ID=\"PSFMAG_I\" datatype=\"float\" name=\"PSFMAG_I\" ucd=\"PHOT_SDSS_I\"/>"
					+ "<FIELD ID=\"PSFMAGERR_I\" datatype=\"float\" name=\"PSFMAGERR_I\" ucd=\"PHOT_SDSS_I ERROR\"/>"
					+ "<FIELD ID=\"PSFMAG_Z\" datatype=\"float\" name=\"PSFMAG_Z\" ucd=\"PHOT_SDSS_Z\"/>"
					+ "<FIELD ID=\"PSFMAGERR_Z\" datatype=\"float\" name=\"PSFMAGERR_Z\" ucd=\"PHOT_SDSS_Z ERROR\"/>"
					+ "<DATA>"
					+ "<TABLEDATA>"
					+ "  <TR>"
					+ "    <TD>587726032792059952</TD>"
					+ "    <TD>195.162958157847</TD>"
					+ "    <TD>2.50144182633295</TD>"
					+ "    <TD>GALAXY</TD>"
					+ "    <TD>13.7583</TD>"
					+ "    <TD>12.79937</TD>"
					+ "    <TD>12.27589</TD>"
					+ "    <TD>11.99715</TD>"
					+ "    <TD>11.71331</TD>"
					+ "    <TD>0.004007341</TD>"
					+ "    <TD>0.001826325</TD>"
					+ "    <TD>0.001757175</TD>"
					+ "    <TD>0.001762709</TD>"
					+ "    <TD>0.002704954</TD>"
					+ "    <TD>17.67873</TD>"
					+ "    <TD>0.0193816</TD>"
					+ "    <TD>16.67595</TD>"
					+ "    <TD>0.03404235</TD>"
					+ "    <TD>16.40215</TD>"
					+ "    <TD>0.06255744</TD>"
					+ "    <TD>16.2727</TD>"
					+ "    <TD>0.02829609</TD>"
					+ "    <TD>16.17118</TD>"
					+ "    <TD>0.07506536</TD>"
					+ "  </TR>"
					+ "</TABLEDATA>"
					+ "</DATA>"
					+ "</TABLE>"
					+ "</RESOURCE>"
					+ "</VOTABLE>";
	
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
		configBean = new SelectRowsActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		//configBean.setTypeOfFilter("Column names");
		
		
		//configBean.setExampleString("something");
		//configBean.setExampleUri(URI.create("http://localhost:8080/myEndPoint"));
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		SelectRowsActivityConfigurationBean invalidBean = new SelectRowsActivityConfigurationBean();
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
		//inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "startsWith(TYPE,\"GALAXY\")");
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
	public void executeAsynchWithStringInputAndStringExpression() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		//inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "startsWith(TYPE,\"GALAXY\")");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		String a = new String(resultSelectRows.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		
		assertTrue("Wrong output : ", (a.length()>b.length()-17) && (a.length()<b.length()+17));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Test
	public void executeAsynchWithNumericExpression() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		//inputs.put(IN_FORMAT_INPUT_TABLE, "votable");
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "U < 15");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectRows.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		
		assertTrue("Wrong output : ", (a.length()>b.length()-17) && (a.length()<b.length()+17));
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
		//inputs.put(IN_FORMAT_INPUT_TABLE, "");
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "U < 15");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectRows.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		
		assertTrue("Wrong output : ", (a.length()>b.length()-17) && (a.length()<b.length()+17));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	@Test(expected = Exception.class)
	public void executeAsynchWithNullInput() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		//inputs.put(IN_FORMAT_INPUT_TABLE, null);
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		//inputs.put(IN_FILTER, "U < 15");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectRows.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		
		assertTrue("Wrong output : ", (a.length()>b.length()-6) && (a.length()<b.length()+6));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));
	}
	
	@Test(expected = Exception.class)
	public void executeAsynchWithInvalidInput() throws Exception {
		configBean.setTypeOfInput("String");
		//configBean.setTypeOfFilter("Column names");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOtable());
		//inputs.put(IN_FORMAT_INPUT_TABLE, null);
		//inputs.put(IN_FORMAT_OUTPUT_TABLE, "ascii");
		inputs.put(IN_FILTER, "dfdf");
		//inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.ascii");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String a = new String(resultSelectRows.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		
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
	
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 3, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
}
