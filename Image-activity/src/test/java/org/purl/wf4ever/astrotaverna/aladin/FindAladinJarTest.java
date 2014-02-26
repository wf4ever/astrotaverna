package org.purl.wf4ever.astrotaverna.aladin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


import net.sf.taverna.raven.prelauncher.ClassLocation;
import net.sf.taverna.t2.activities.testutils.ActivityInvoker;
import net.sf.taverna.t2.annotation.AnnotationAssertion;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import cds.aladin.Aladin;

/** 
 * 
 * @author julian Garrido 
 * Some tests may fail because the resulting votable name comes from a random number 
 */
public class FindAladinJarTest {
	private AladinScriptParser parser;

	//this method is invoked before each test method
	@Before
	public void intTest() throws Exception {
	}
	
	@Test
	public void findAladin() throws IOException{
		File file = ClassLocation.getClassLocationFile(Aladin.class);
		System.out.println(file);
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getCanonicalPath());
		System.out.println(file.toString());
		assertTrue(!file.getAbsolutePath().isEmpty());
		assertTrue(file.getAbsolutePath().endsWith("Aladin-7.5.jar"));
        
        //System.out.println("--"+file.getAbsolutePath()+"--");
	}
	
	@Test
	public void findOtherClassAndBuildAladinPath() throws IOException{
		File file = ClassLocation.getClassLocationFile(AnnotationAssertion.class);
		System.out.println(file);
		assertTrue(!file.getAbsolutePath().isEmpty());
		assertTrue(file.getAbsolutePath().contains("workflowmodel-api"));
		String path = file.getAbsolutePath();
		int position = path.indexOf("repository");
//		System.out.println("net"+File.separator+"sf"+File.separator+"taverna"+File.separator+"core");
		position = path.indexOf("net"+File.separator+"sf"+File.separator+"taverna"+File.separator+"t2"+File.separator+"core");
		//System.out.println(position);
		String ALADINJAR = path.substring(0, position);
		ALADINJAR += "cds"+File.separator+"aladin"+File.separator+"Aladin"+File.separator+"7.5"+File.separator+"Aladin-7.5.jar";
        System.out.println(ALADINJAR);
        //System.out.println("--"+file.getAbsolutePath()+"--");
	}
}
