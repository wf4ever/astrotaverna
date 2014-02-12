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
public class AddColumnByExpressionActivityTest {

	private AddColumnByExpressionActivityConfigurationBean configBean;
	
	//this variables must be the same than the ones defined at AddColumnByExpressionActivity.java
	private static final String IN_FIRST_INPUT_TABLE = "voTable";
	private static final String IN_NAME_NEW_COL = "nameNewCol";
	private static final String IN_FILTER = "expression";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";

	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private AddColumnByExpressionActivity activity = new AddColumnByExpressionActivity();

	
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
		configBean = new AddColumnByExpressionActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");
		
	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		AddColumnByExpressionActivityConfigurationBean invalidBean = new AddColumnByExpressionActivityConfigurationBean();
		invalidBean.setTypeOfInput("another thing");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration2() throws ActivityConfigurationException {
		AddColumnByExpressionActivityConfigurationBean invalidBean = new AddColumnByExpressionActivityConfigurationBean();
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
		inputs.put(IN_NAME_NEW_COL, "newCol");
		inputs.put(IN_FILTER, "raFK4toFK5radians(ra, dec)");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documents/wf4ever/tables/resultTable.xml");
		
		//function parameters
		//inputs.put("RA", "ra");
		//inputs.put("DEC", "dec");

		
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
		inputs.put(IN_FILTER, "raFK4toFK5radians(ra, dec)");
		//inputs.put(IN_FILTER, "ra + dec");
		inputs.put(IN_NAME_NEW_COL, "newCol");


		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();

		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		try{
			outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		
		String a = new String(resultAddColumnByExpression.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
				
		TestUtils.compareStringLengthsIgnoreWhiteSpace(a, b, 6);
		}catch(Exception ex){System.out.println(ex.toString());}
		
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
	}
	

	@Test(expected = RuntimeException.class)
	public void executeAsynchWithNullInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOTable2());
		inputs.put(IN_FILTER, "raFK4toFK5radians2(ra, dec)");
		//inputs.put(IN_NAME_NEW_COL, "newCol");

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put("simpleOutput", String.class);
		//expectedOutputTypes.put("moreOutputs", String.class);
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		
		String a = new String(resultAddColumnByExpression.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
				
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
				
		assertTrue("Wrong output: ", (a.length()>b.length()-6) && (a.length()<b.length()+6));
		assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	

	@Test(expected = RuntimeException.class)
	public void executeAsynchWithInvalidInput() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_INPUT_TABLE, MyUtils.getExampleVOTable2());
		inputs.put(IN_FILTER, "raFK4toFK5radians2(ra, thisisnotacolumn)");
		inputs.put(IN_NAME_NEW_COL, "newCol");
		
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = null;
		outputs= ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);
		
		
		String a = new String(resultAddColumnByExpression.toCharArray());
		String b = new String(((String)outputs.get(OUT_SIMPLE_OUTPUT)).toCharArray());
		
				
		a = a.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
		b = b.replace("\n", "").replace("\t", "").replace(" ", "").replace(System.getProperty("line.separator"), "");
				
		assertTrue("Wrong output: ", (a.length()>b.length()-6) && (a.length()<b.length()+6));	
		assertEquals("simple-report", outputs.get(OUT_REPORT));


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
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	
		
		
		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 4, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	private static final String resultAddColumnByExpression = 	
			"<?xml version='1.0'?>"
					+ "<VOTABLE version=\"1.1\""
					+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
					+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
					+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
					+ "<!--"
					+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
					+ " !  at 2012-05-18T10:44:23"
					+ " !-->"
					+ "<RESOURCE>"
					+ "<TABLE name=\"astro8330722452932962800.tmp\" nrows=\"3\">" // the name comes from a random number. it should be different
					+ "<DESCRIPTION>"
					+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
					+ "</DESCRIPTION>"
					+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
					+ "<FIELD datatype=\"int\" name=\"unique_id\">"
					+ "<DESCRIPTION>Integer key</DESCRIPTION>"
					+ "<VALUES null='-2147483648'/>"
					+ "</FIELD>"
					+ "<FIELD arraysize=\"*\" datatype=\"char\" name=\"name\" ucd=\"ID_MAIN\">"
					+ "<DESCRIPTION>FIRST Source Designation</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"ra\" ucd=\"POS_EQ_RA_MAIN\" unit=\"degree\">"
					+ "<DESCRIPTION>Right Ascension</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"dec\" ucd=\"POS_EQ_DEC_MAIN\" unit=\"degree\">"
					+ "<DESCRIPTION>Declination</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
					+ "<DESCRIPTION>Peak Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"flux_20_cm_error\" ucd=\"stat.error;phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
					+ "<DESCRIPTION>Local Noise Estimate of Source (mJy)</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"int_flux_20_cm\" ucd=\"phot.flux.density;em.radio.750-1500MHz\" unit=\"mJy\">"
					+ "<DESCRIPTION>Integrated Flux Density at 1.4GHz (mJy)</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"sidelobe_prob\" ucd=\"stat.probability\">"
					+ "<DESCRIPTION>Probability That Source Is a Sidelobe</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"twomass_first_offset\" ucd=\"pos.angDistance;em.IR;em.radio.750-1500MHz\" unit=\"arcsec\">"
					+ "<DESCRIPTION>Offset of Nearest 2MASS Source</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"twomass_kmag\" ucd=\"phot.mag;em.IR.K\" unit=\"mag\">"
					+ "<DESCRIPTION>K Magnitude of Nearest 2MASS Source</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"Search_Offset\" unit=\"'\">"
					+ "<DESCRIPTION>Offset of target/observation from query center</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"newCol\"/>"
					+ "<DATA>"
					+ "<TABLEDATA>"
					+ "  <TR>"
					+ "    <TD>946464</TD>"
					+ "    <TD>FIRST J233859.7-112355</TD>"
					+ "    <TD>354.749046</TD>"
					+ "    <TD>-11.398828</TD>"
					+ "    <TD>1.68</TD>"
					+ "    <TD>0.14</TD>"
					+ "    <TD>1.75</TD>"
					+ "    <TD>0.016</TD>"
					+ "    <TD>NaN</TD>"
					+ "    <TD>NaN</TD>"
					+ "    <TD>0.0</TD>"
					+ "    <TD>2.9045820502288446</TD>"
					+ "  </TR>"
					+ "  <TR>"
					+ "    <TD>946352</TD>"
					+ "    <TD>FIRST J233916.9-111928</TD>"
					+ "    <TD>354.820408</TD>"
					+ "    <TD>-11.324472</TD>"
					+ "    <TD>1.62</TD>"
					+ "    <TD>0.138</TD>"
					+ "    <TD>1.48</TD>"
					+ "    <TD>0.047</TD>"
					+ "    <TD>0.12</TD>"
					+ "    <TD>14.99</TD>"
					+ "    <TD>6.126</TD>"
					+ "    <TD>2.9756390660036365</TD>"
					+ "  </TR>"
					+ "  <TR>"
					+ "    <TD>946331</TD>"
					+ "    <TD>FIRST J233846.4-111841</TD>"
					+ "    <TD>354.693467</TD>"
					+ "    <TD>-11.311506</TD>"
					+ "    <TD>11.41</TD>"
					+ "    <TD>0.137</TD>"
					+ "    <TD>15.56</TD>"
					+ "    <TD>0.014</TD>"
					+ "    <TD>NaN</TD>"
					+ "    <TD>NaN</TD>"
					+ "    <TD>6.176</TD>"
					+ "    <TD>2.8506099560359695</TD>"
					+ "  </TR>"
					+ "</TABLEDATA>"
					+ "</DATA>"
					+ "</TABLE>"
					+ "</RESOURCE>"
					+ "</VOTABLE>";
}
