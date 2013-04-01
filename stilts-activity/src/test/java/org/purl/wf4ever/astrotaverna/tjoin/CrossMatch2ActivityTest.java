package org.purl.wf4ever.astrotaverna.tjoin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
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
import org.purl.wf4ever.astrotaverna.utils.MyUtils;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;

public class CrossMatch2ActivityTest {

	private CrossMatch2ActivityConfigurationBean configBean;

	//these variables must be the same than the ones defined in the activity class
	private static final String IN_FIRST_TABLE = "VOTable1";
	private static final String IN_SECOND_TABLE = "VOTable2";
	private static final String IN_MATCHER = "matcher";
	private static final String IN_PARAMS = "params";
	private static final String IN_VALUES_1 = "values1";
	private static final String IN_VALUES_2 = "values2";
	private static final String IN_TUNING = "tuning";
	private static final String IN_SCORECOL = "scoreCol";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	
	private static final String OUT_SIMPLE_OUTPUT = "outputTable";
	private static final String OUT_REPORT = "report";
	
	private CrossMatch2Activity activity = new CrossMatch2Activity();

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
		configBean = new CrossMatch2ActivityConfigurationBean();
		
		configBean.setTypeOfInput("File");

	}

	@Test(expected = ActivityConfigurationException.class)
	public void invalidConfiguration() throws ActivityConfigurationException {
		CrossMatch2ActivityConfigurationBean invalidBean = new CrossMatch2ActivityConfigurationBean();
		invalidBean.setTypeOfInput("Fileon");
		// Should throw ActivityConfigurationException
		activity.configure(invalidBean);
	}
	
	

	//this test is valid only with the right folders

	@Test
	public void executeAsynch() throws Exception {
		configBean.setTypeOfInput("File");
		activity.configure(configBean);

		File outputfile = File.createTempFile("astro", null);
		outputfile.deleteOnExit();
		File tmpInFile = MyUtils.writeStringAsTmpFile(table2);
		tmpInFile.deleteOnExit();
		String table2Path = tmpInFile.getAbsolutePath();
		
		File tmpInFile2 = MyUtils.writeStringAsTmpFile(table3);
		tmpInFile2.deleteOnExit();
		String table3Path = tmpInFile2.toString();
		
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_TABLE, table2Path);
		inputs.put(IN_SECOND_TABLE, table3Path);
		inputs.put(this.IN_VALUES_1,"ra dec");
		inputs.put(this.IN_VALUES_2,"ra dec");
		inputs.put(this.IN_PARAMS,"2");
		inputs.put(IN_OUTPUT_TABLE_NAME, outputfile.getAbsolutePath());
		//inputs.put(this.IN_TUNING, "15");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		StarTable starTable = loadVOTable(outputfile.getAbsolutePath());
		
		// Should not change on reconfigure
		assertEquals("Unexpected number of rows.", 1, starTable.getRowCount());
		assertEquals("Unexpected number of columns.", 20, starTable.getColumnCount());
		
		//assertEquals("/home/julian/Documentos/wf4ever/tables/join_test.xml", outputs.get(OUT_SIMPLE_OUTPUT));
		//assertEquals("simple-report", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test(expected = Exception.class)
	public void executeAsynchWithUnexistingFile() throws Exception {
		configBean.setTypeOfInput("File");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
	
		inputs.put(IN_FIRST_TABLE, "/home/julian/Documentos/wf4ever/tables/filenoexist.xml");
		inputs.put(IN_SECOND_TABLE, "/home/julian/Documentos/wf4ever/tables/filenoexist2.xml");
		inputs.put(this.IN_VALUES_1,"ra dec");
		inputs.put(this.IN_VALUES_2,"ra dec");
		inputs.put(this.IN_PARAMS,"2");
		inputs.put(IN_OUTPUT_TABLE_NAME, "/home/julian/Documentos/wf4ever/tables/file.xml");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

				
		

	}

	
	@Test
	public void executeAsynchWitStrings() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_TABLE, table2);
		inputs.put(IN_SECOND_TABLE, table3);
		inputs.put(this.IN_VALUES_1,"ra dec");
		inputs.put(this.IN_VALUES_2,"ra dec");
		inputs.put(this.IN_PARAMS,"2");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		File tmpInFile = MyUtils.writeStringAsTmpFile(result);
		tmpInFile.deleteOnExit();
		
		StarTable starTable = loadVOTable(tmpInFile.getAbsolutePath());
		
		// Should not change on reconfigure
		assertEquals("Unexpected number of rows", 1, starTable.getRowCount());
		assertEquals("Unexpected number of columns", 20, starTable.getColumnCount());
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	@Test(expected = Exception.class)
	public void executeAsynchWitNullInport() throws Exception {
		configBean.setTypeOfInput("String");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put(IN_FIRST_TABLE, table2);
		//inputs.put(IN_SECOND_TABLE, table3);
		inputs.put(this.IN_VALUES_1,"ra dec");
		inputs.put(this.IN_VALUES_2,"ra dec");
		inputs.put(this.IN_PARAMS,"2");
		
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		String result = (String) outputs.get(OUT_SIMPLE_OUTPUT);
		
		File tmpInFile = MyUtils.writeStringAsTmpFile(result);
		tmpInFile.deleteOnExit();
		
		StarTable starTable = loadVOTable(tmpInFile.getAbsolutePath());
		
		// Should not change on reconfigure
		assertEquals("Unexpected number of rows", 2, starTable.getRowCount());
		assertEquals("Unexpected number of columns", 7, starTable.getColumnCount());

	}
	
	@Test
	public void reConfiguredActivity() throws Exception {
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());

		activity.configure(configBean);
		assertEquals("Unexpected inputs", 7, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());

		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 7, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}

	
	@Test
	public void reConfiguredPorts() throws Exception {
		activity.configure(configBean);

		CrossMatch2ActivityConfigurationBean specialBean = new CrossMatch2ActivityConfigurationBean();
		specialBean.setTypeOfInput("String");

		activity.configure(specialBean);		
		// Should now have added the optional ports
		assertEquals("Unexpected inputs", 6, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 2, activity.getOutputPorts().size());
	}
	
	
	public StarTable loadVOTable( String  path ) throws IOException {
	    return new StarTableFactory().makeStarTable(path, "votable" );
	}
	
	private String table1 = "<?xml version='1.0'?>"
			+ "<VOTABLE version=\"1.1\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
			+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
			+ "<!--"
			+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
			+ " !  at 2012-05-20T11:42:06"
			+ " !-->"
			+ "<RESOURCE>"
			+ "<TABLE nrows=\"1\">"
			+ "<DESCRIPTION>"
			+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
			+ "</DESCRIPTION>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_1\">"
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
			+ "<FIELD datatype=\"int\" name=\"unique_id_2\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
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
			+ "<DATA>"
			+ "<TABLEDATA>"
			+ "  <TR>"
			+ "    <TD>946464</TD>"
			+ "    <TD>FIRST J233859.7-112355</TD>"
			+ "    <TD>354.749046</TD>"
			+ "    <TD>-11.398828</TD>"
			+ "    <TD>946464</TD>"
			+ "    <TD>1.68</TD>"
			+ "    <TD>0.14</TD>"
			+ "    <TD>1.75</TD>"
			+ "    <TD>0.016</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>0.0</TD>"
			+ "  </TR>"
			+ "</TABLEDATA>"
			+ "</DATA>"
			+ "</TABLE>"
			+ "</RESOURCE>"
			+ "</VOTABLE>";
	
	
	
	private String table2 = "<?xml version='1.0'?>"
			+ "<VOTABLE version=\"1.1\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
			+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
			+ "<!--"
			+ " !  VOTable written by STIL version 3.0-3 (uk.ac.starlink.votable.VOTableWriter)"
			+ " !  at 2012-05-20T11:42:06"
			+ " !-->"
			+ "<RESOURCE>"
			+ "<TABLE nrows=\"2\">"
			+ "<DESCRIPTION>"
			+ "Faint Images of the Radio Sky at Twenty cm (FIRST)"
			+ "</DESCRIPTION>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<PARAM arraysize=\"18\" datatype=\"char\" name=\"default_search_radius\" ucd=\"OBS_ANG-SIZE\" value=\"0.0166666666666667\"/>"
			+ "<FIELD datatype=\"int\" name=\"unique_id_1\">"
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
			+ "<FIELD datatype=\"int\" name=\"unique_id_2\">"
			+ "<DESCRIPTION>Integer key</DESCRIPTION>"
			+ "<VALUES null='-2147483648'/>"
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
			+ "<DATA>"
			+ "<TABLEDATA>"
			+ "  <TR>"
			+ "    <TD>946352</TD>"
			+ "    <TD>FIRST J233916.9-111928</TD>"
			+ "    <TD>354.820408</TD>"
			+ "    <TD>-11.324472</TD>"
			+ "    <TD>946352</TD>"
			+ "    <TD>1.62</TD>"
			+ "    <TD>0.138</TD>"
			+ "    <TD>1.48</TD>"
			+ "    <TD>0.047</TD>"
			+ "    <TD>0.12</TD>"
			+ "    <TD>14.99</TD>"
			+ "    <TD>6.126</TD>"
			+ "  </TR>"
			+ "  <TR>"
			+ "    <TD>946331</TD>"
			+ "    <TD>FIRST J233846.4-111841</TD>"
			+ "    <TD>354.693467</TD>"
			+ "    <TD>-11.311506</TD>"
			+ "    <TD>946331</TD>"
			+ "    <TD>11.41</TD>"
			+ "    <TD>0.137</TD>"
			+ "    <TD>15.56</TD>"
			+ "    <TD>0.014</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>NaN</TD>"
			+ "    <TD>6.176</TD>"
			+ "  </TR>"
			+ "</TABLEDATA>"
			+ "</DATA>"
			+ "</TABLE>"
			+ "</RESOURCE>"
			+ "</VOTABLE>";
	
	private static final String table3 = 
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
					+ "<TABLE name=\"table3.xml\" nrows=\"2\">" 
					+ "<PARAM datatype=\"float\" name=\"inputRA\" unit=\"degrees\" value=\"195.16333\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputDEC\" unit=\"degrees\" value=\"2.5007777\"/>"
					+ "<PARAM datatype=\"float\" name=\"inputSR\" unit=\"degrees\" value=\"0.001\"/>"
					+ "<PARAM arraysize=\"1\" datatype=\"char\" name=\"rowcount, table 0\" value=\"2\"/>"
					+ "<FIELD datatype=\"double\" name=\"ra\" ucd=\"POS_EQ_RA_MAIN\" unit=\"degree\">"
					+ "<DESCRIPTION>Right Ascension</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD datatype=\"double\" name=\"dec\" ucd=\"POS_EQ_DEC_MAIN\" unit=\"degree\">"
					+ "<DESCRIPTION>Declination</DESCRIPTION>"
					+ "</FIELD>"
					+ "<FIELD ID=\"U\" datatype=\"float\" name=\"U\" ucd=\"PHOT_SDSS_U FIT_PARAM\"/>"
					+ "<FIELD ID=\"G\" datatype=\"float\" name=\"G\" ucd=\"PHOT_SDSS_G FIT_PARAM\"/>"
					+ "<FIELD ID=\"R\" datatype=\"float\" name=\"R\" ucd=\"PHOT_SDSS_R FIT_PARAM\"/>"
					+ "<FIELD ID=\"I\" datatype=\"float\" name=\"I\" ucd=\"PHOT_SDSS_I FIT_PARAM\"/>"
					+ "<FIELD ID=\"Z\" datatype=\"float\" name=\"Z\" ucd=\"PHOT_SDSS_Z FIT_PARAM\"/>"
					+ "<DATA>"
					+ "<TABLEDATA>"
					+ "  <TR>"
					+ "    <TD>354.820408</TD>"
					+ "    <TD>-11.324472</TD>"
					+ "    <TD>17.52193</TD>"
					+ "    <TD>17.47281</TD>"
					+ "    <TD>17.50826</TD>"
					+ "    <TD>17.99788</TD>"
					+ "    <TD>17.8128</TD>"
					+ "  </TR>"
					+ "  <TR>"
					+ "    <TD>354.820408</TD>"
					+ "    <TD>-11.324472</TD>"
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
