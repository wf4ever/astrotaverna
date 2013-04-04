package org.purl.wf4ever.astrotaverna.aladin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/** 
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class AladinScriptParserTest {
	private AladinScriptParser parser;

	//this method is invoked before each test method
	@Before
	public void intTest() throws Exception {
		parser = new AladinScriptParser();
	}
	
	@Test
	public void parseSaveScript(){
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
		String script = "get aladin(J,FITS) m1 ;\n save -png -jpg /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg -lk; backup /users/juan\\juan.aj; save m1.eps ; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampletests/m1.jpg; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/example&tests/m1.jpg; quit";
		ArrayList<String> list = parser.parseScript(script);

		System.out.println(list.toString());
		
		assertEquals("Unexpected number of elemens", 3, list.size());
		if(list.size()>1){
			assertEquals("Unexpected filename", "/Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg", list.get(0));
			assertEquals("Unexpected filename", "/users/juan\\juan.aj", list.get(1));
			assertEquals("Unexpected filename", "m1.eps", list.get(2));
		}
	}
	
	@Test
	public void parseExportScript(){
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
		String script = "get aladin(J,FITS) m1 ;\n export DSS1.V.SERC C:\\DSS2image.fits; export -votable GSC1.2 /home/gsc1.2.dat; export RGBimg m1RGB.fits; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampletests/m1.jpg; quit";
		//String script = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/example&tests/m1.jpg; quit";
		ArrayList<String> list = parser.parseScript(script);

		assertEquals("Unexpected number of elemens", list.size(), 3);
		if(list.size()>=3){
			assertEquals("Unexpected filename", "C:\\DSS2image.fits", list.get(0));
			assertEquals("Unexpected filename", "/home/gsc1.2.dat", list.get(1));
			assertEquals("Unexpected filename", "m1RGB.fits", list.get(2));
		}
	}
	
	@Test
	public void parseFileScript(){
		
	}
	
	
	@Test
	public void parseURLScript() throws Exception {
		ArrayList<String> list = parser.parseURL("http://cdsweb.u-strasbg.fr/~allen/CDS_Tutorial/attachments/Arp_script.ajs");
		
		assertEquals("Unexpected number of elemens", 2,  list.size());
		if(list.size()>0){
			assertEquals("Unexpected filename", "/Users/allen/Desktop/Arp/Arp-$2_chart.png", list.get(0));
		}	}

	/*
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
	*/
}
