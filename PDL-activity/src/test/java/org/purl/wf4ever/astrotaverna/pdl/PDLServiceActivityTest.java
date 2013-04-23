package org.purl.wf4ever.astrotaverna.pdl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import org.purl.wf4ever.astrotaverna.utils.MyUtils;

//http://pdl-calc.obspm.fr:8081/broadening/pdlDescription/PDL-Description.xml

//http://pdl-calc.obspm.fr:8081/broadening//getJobInfo?mail=carlo-maria.zwolf@obspm.fr&jobId=1&userId=4

//http://askubuntu.com/questions/43846/how-to-put-a-trigger-on-a-directory

//https://github.com/cmzwolf/OnlineCodeDaemon/blob/master/src/net/ivoa/oc/daemon/jobProcessor/JobProcessor.java


public class PDLServiceActivityTest {

	private PDLServiceActivityConfigurationBean configBean;

	//these variables must be the same than the ones defined in the activity class
	private static final String IN_FIRST_INPUT = "votable1";
	private static final String IN_SECOND_INPUT = "votable2";
	private static final String IN_OUTPUT_TABLE_NAME = "outputFileNameIn";
	private static final String OUT_SIMPLE_OUTPUT = "outputFileOut";
	private static final String OUT_REPORT = "status";
	private static final String RESPONSE_BODY = "response_body";
	private static final String DEFAULT_OUTPUT = "fileResult";
	
	private PDLServiceActivity activity = new PDLServiceActivity();

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
		configBean = new PDLServiceActivityConfigurationBean();
		//configBean.setPdlDescriptionFile("/home/julian/otherworkspaces/pdlworkspace/testPDLcmdLineTool/PDL-Description.xml");
		activity = new PDLServiceActivity();
	}


	//it doesn't throw a exception because they have to be captured in the activity
	@Test()
	public void invalidConfiguration() throws ActivityConfigurationException{
		PDLServiceActivityConfigurationBean invalidBean = new PDLServiceActivityConfigurationBean();
		invalidBean.setPdlDescriptionFile("home/PDL-Description.xml");
		activity.configure(invalidBean);
	}
	
	//if there is not config file, there is no inputs: it has to fail
	@Test(expected = java.lang.RuntimeException.class)
	public void runWithInvalidConfig() throws Exception {
		
		configBean.setPdlDescriptionFile(" ");
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
	
		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 1, outputs.size());
		assertEquals("Not valid", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}



	//test with not valid input: the float is 1/12.0 instead of 1/15.0
	//¿PQ LANZA ESTE UNA EXCEPCION?
	
	@Test(expected = java.lang.RuntimeException.class)
	public void executeAsynchNotValid() throws Exception {
		InputStream is = this.getClass().getResourceAsStream("/org/purl/wf4ever/astrotaverna/pdl/PDL-DescriptionTest.xml");
	    String pdlContent = MyUtils.convertStreamToString(is);
	    File tmpFile = MyUtils.writeStringAsTmpFile(pdlContent);
		configBean.setPdlDescriptionFile(tmpFile.getAbsolutePath());
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		Float value = new Float(1/12.0);  
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
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);
		
		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 1, outputs.size());
		assertEquals("With error", outputs.get(OUT_REPORT));

	}
	
	//THIS IS USING LOCAL FILES
	@Ignore
	@Test
	public void executeAsynchValid() throws Exception {
		URL url1 = this.getClass().getResource("/org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml");
		URL url2 = this.getClass().getResource("/PDL-activity/src/test/java/org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml");
		
		InputStream is = this.getClass().getResourceAsStream("/org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml");
	    String pdlContent = MyUtils.convertStreamToString(is);
	    File tmpFile = MyUtils.writeStringAsTmpFile(pdlContent);
		configBean.setPdlDescriptionFile(tmpFile.getAbsolutePath());
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
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 1, outputs.size());
		assertEquals("Valid", outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}

	@Ignore
	@Test
	public void executeBroadeningService() throws Exception {
		String serviceURL = "http://pdl-calc.obspm.fr:8081/broadening/pdlDescription/PDL-Description.xml";
			    
		configBean.setPdlDescriptionFile(serviceURL);
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		Float value = new Float(2/3.0);
		inputs.put("Density", value.toString());
		inputs.put("InitialLevel", "1");
		inputs.put("FinalLevel", "3");
		inputs.put("Temperature", "15");
		inputs.put("mail", "jgarrido@iaa.es");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);
		

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		assertEquals("Unexpected outputs", 2, outputs.size());
		assertEquals(PDLServiceController.getValidStatus(), outputs.get(OUT_REPORT));
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	//This test was for the service at Paris. Now It has been moved to IAA
	@Ignore 
	@Test
	public void executeMontageTavernaEntryPointService() throws Exception {
		String serviceURL = "http://pdl-calc.obspm.fr:8081/montage/pdlDescription/PDL-Description.xml";
			    
		configBean.setPdlDescriptionFile(serviceURL);
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		Float value = new Float(2/3.0);
		inputs.put("NAXIS1", "2259");
		inputs.put("NAXIS2", "2199");
		inputs.put("CTYPE1", "RA---TAN");
		inputs.put("CTYPE2", "DEC--TAN");
		inputs.put("CRVAL1", "210.835222357");
		inputs.put("CRVAL2", "54.367562188");
		inputs.put("CDELT1", "-0.000277780");
		inputs.put("CDELT2", "0.000277780");
		inputs.put("CRPIX1", "1130");
		inputs.put("CRPIX2", "1100");
		inputs.put("CROTA2", "-0.052834593"); //orig value: -0.052834592
		inputs.put("EQUINOX", "2000");
		inputs.put("ImageLocation", "SampleLocation");
		inputs.put("mail", "tetrarquis@gmail.com");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);
		

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		int expectedoutputs=2;
		if(outputs.size()==3)
			expectedoutputs=3;
		assertEquals("Unexpected outputs", expectedoutputs, outputs.size());//only if the result
		assertTrue("Invalid or error status", PDLServiceController.getPendingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0 
				                  || PDLServiceController.getFinishedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getCompletedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getRunningStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getExecutingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0);
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	@Test
	public void executeIAAMontageTavernaEntryPointService() throws Exception {
		String serviceURL = "http://dae81.iaa.es:8081/montage/pdlDescription/PDL-Description.xml";
			    
		configBean.setPdlDescriptionFile(serviceURL);
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		Float value = new Float(2/3.0);
		inputs.put("NAXIS1", "2259");
		inputs.put("NAXIS2", "2199");
		inputs.put("CTYPE1", "RA---TAN");
		inputs.put("CTYPE2", "DEC--TAN");
		inputs.put("CRVAL1", "210.835222357");
		inputs.put("CRVAL2", "54.367562188");
		//inputs.put("CDELT1", "-0.000277780");
		inputs.put("CDELT1", "-0.000279780");
		inputs.put("CDELT2", "0.000277780");
		inputs.put("CRPIX1", "1130");
		inputs.put("CRPIX2", "1100");
		inputs.put("CROTA2", "-0.052834593"); //orig value: -0.052834592
		inputs.put("EQUINOX", "2000");
		inputs.put("ImageLocation", "SampleLocation");
		inputs.put("mail", "jgarrido@iaa.es");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);
		

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(
				activity, inputs, expectedOutputTypes);

		int expectedoutputs=2;
		if(outputs.size()==3)
			expectedoutputs=3;
		assertEquals("Unexpected outputs", expectedoutputs, outputs.size());//only if the result
		assertTrue("Invalid or error status", PDLServiceController.getPendingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0 
				                  || PDLServiceController.getFinishedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getCompletedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getRunningStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getExecutingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0);
		System.out.println("Status: "+ (String)outputs.get(OUT_REPORT));
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	//commented only to not overload the Paris Service
	@Ignore
	@Test
	public void executeParisDurhamTavernaEntryPointService() throws Exception {
		String serviceURL = "http://pdl-calc.obspm.fr:8081/ParisDurham/pdlDescription/PDL-Description.xml";
			    
		configBean.setPdlDescriptionFile(serviceURL);
		activity.configure(configBean);

		Map<String, Object> inputs = new HashMap<String, Object>();
		//Double value = new Double(0.00000000000001);
		Double value = new Double(1.0E-15);
		System.out.println("Float value: " + value.toString());
		//The service doesn't converge with the following set of parameters(commented)
		/*
		//parameter group
		inputs.put("NstepMax", "5000");
		inputs.put("NstepW", "300");
		inputs.put("NH2Lev", "12");
		inputs.put("NH2Lines", "20");
		inputs.put("iforH2", "2");
		inputs.put("ikinH2", "2");
		inputs.put("xll", "1");
		inputs.put("epsV", value.toString());
		inputs.put("TimeJ", "600");
		inputs.put("durationMax", "1000");
		//parameter group
		//inputs.put("Zeta", "0,00000000000001");
		inputs.put("Zeta", value.toString());
		//parameter group
		inputs.put("shockType", "C");
		inputs.put("Nfluids", "2");
		inputs.put("Bbeta", "1");
		inputs.put("Vs", "200");
		inputs.put("Vdi", "250");
		inputs.put("OpH2", "100");
		inputs.put("Ti", "400");
		inputs.put("nHi", "1");
		inputs.put("Tg", "220");
		//parameter group
		inputs.put("SOS", "CD");
		inputs.put("LEOS", "CD");
		inputs.put("LIOS", "local");
		*/
		//numerical parameters
		inputs.put("NstepMax", "5000");//-
		inputs.put("NstepW", "5");//-
		inputs.put("NH2Lev", "150");//-
		inputs.put("NH2Lines", "200");//-
		inputs.put("iforH2", "1");//-
		inputs.put("ikinH2", "2");//-
		inputs.put("xll", "1.00E9");//-
		inputs.put("epsV", "1.00E-8");//-
		inputs.put("TimeJ", "2.00E10");//-
		inputs.put("durationMax", "1.00E5");//-
		//H_h2_flag --->BOTH
		//Force_I_C --> 1: Force Ion Conservation
		//parameter group
		//inputs.put("Zeta", "0,00000000000001");
		inputs.put("Zeta", "5.0E-17");//-
		//shock parameters
		inputs.put("shockType", "C");//-
		inputs.put("Nfluids", "3");//-
		inputs.put("Bbeta", "1.0");//-
		inputs.put("Vs", "25.0");//-
		inputs.put("Vdi", "1000.0");
		inputs.put("OpH2", "3.0");//-
		inputs.put("Ti", "10");//-
		inputs.put("nHi", "10000.0");//-
		inputs.put("Tg", "15");//-
		//Cool_KN -> 1
		//output specifications
		inputs.put("SOS", "FD");//-
		inputs.put("LEOS", "ln(N/g)");//-
		inputs.put("LIOS", "local");//-
		//others
		inputs.put("mail", "tetrarquis@gmail.com");
		

		Map<String, Class<?>> expectedOutputTypes = new HashMap<String, Class<?>>();
		//expectedOutputTypes.put(OUT_SIMPLE_OUTPUT, String.class);
		expectedOutputTypes.put(OUT_REPORT, String.class);
		expectedOutputTypes.put(RESPONSE_BODY, String.class);
		expectedOutputTypes.put(DEFAULT_OUTPUT, String.class);
		

		Map<String, Object> outputs = ActivityInvoker.invokeAsyncActivity(activity, inputs, expectedOutputTypes);

		int expectedoutputs=3;
		
		
		assertEquals("Unexpected outputs", expectedoutputs, outputs.size());//only if the result
		
		assertTrue("Invalid or error status", PDLServiceController.getPendingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0 
				                  || PDLServiceController.getFinishedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getCompletedStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getRunningStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0
				                  || PDLServiceController.getExecutingStatus().compareTo((String)outputs.get(OUT_REPORT)) ==0);
		
		System.out.println("-------------------------------------");
		System.out.println(outputs.get(RESPONSE_BODY));
		System.out.println("-------------------------------------");
		
		//assertEquals(Arrays.asList("Value 1", "Value 2"), outputs
		//		.get("moreOutputs"));

	}
	
	
	//THIS IS USING LOCAL FILES
	
	@Test
	public void reConfiguredActivity() throws Exception {
		
		
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
		
		configBean.setPdlDescriptionFile("");
		activity.configure(configBean);
		
		assertEquals("Unexpected inputs", 0, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 0, activity.getOutputPorts().size());
		
		//InputStream is = this.getClass().getResourceAsStream("/org/purl/wf4ever/astrotaverna/pdl/PDL-DescriptionTest.xml");
		//String pdlUrl = this.getClass().getResource("/org/purl/wf4ever/astrotaverna/pdl/PDL-DescriptionTest.xml").toExternalForm();
	    //String pdlContent = MyUtils.convertStreamToString(is);
	    //File tmpFile = MyUtils.writeStringAsTmpFile(pdlContent);
		//configBean.setPdlDescriptionFile(tmpFile.getAbsolutePath());
		System.out.println(PDLServiceActivityTest.class.getResource("/PDL-descriptionTest.xml"));
		System.out.println(PDLServiceActivityTest.class.getResource("org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml"));
		System.out.println(PDLServiceActivityTest.class.getResource("/org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml"));
		String pdlUrl = this.getClass().getResource("/org/purl/wf4ever/astrotaverna/pdl/PDL_DescriptionTest.xml").toExternalForm();
		configBean.setPdlDescriptionFile(pdlUrl);
		
		activity.configure(configBean);
		assertEquals("Unexpected inputs", 16, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 3, activity.getOutputPorts().size());

		activity.configure(configBean);
		// Should not change on reconfigure
		assertEquals("Unexpected inputs", 16, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 3, activity.getOutputPorts().size());
		Iterator<ActivityInputPort> it = activity.getInputPorts().iterator();
		
		/*
		//configBean.setPdlDescriptionFile("http://pdl-calc.obspm.fr:8081/broadening/pdlDescription/PDL-Description.xml");
		configBean.setPdlDescriptionFile("/home/julian/Documents/wf4ever/pdl/wf/PDL-Description-broadening.xml");
		activity.configure(configBean);
		//System.out.println("----------- "+activity.getInputPorts().size());
		//System.out.println(activity.getRestrictionsOnGroups());
		assertEquals("Unexpected inputs", 5, activity.getInputPorts().size());
		assertEquals("Unexpected outputs", 1, activity.getOutputPorts().size());
		*/
	}
	
	
}
