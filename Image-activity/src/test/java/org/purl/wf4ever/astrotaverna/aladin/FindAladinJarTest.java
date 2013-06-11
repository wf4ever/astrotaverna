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
		assertTrue(!file.getAbsolutePath().isEmpty());
		assertTrue(file.getAbsolutePath().endsWith("Aladin.jar"));
        //System.out.println(file);//ValueDifferentOf
        //System.out.println("--"+file.getAbsolutePath()+"--");
	}
}
