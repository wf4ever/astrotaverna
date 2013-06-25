package org.purl.wf4ever.astrotaverna.aladin;

//import static org.junit.Assert.assertTrue;

//import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Map;

import net.sf.taverna.raven.prelauncher.ClassLocation;
import net.sf.taverna.t2.annotation.AnnotationAssertion;

import org.apache.log4j.Logger;
import org.purl.wf4ever.astrotaverna.utils.NoExitSecurityManager_;
import org.purl.wf4ever.astrotaverna.utils.StreamReaderAsync;

//import cds.aladin.Aladin;


/**
 * It runs Aladin scripts and macros. 
 * The version of Aladin is hard coded. At the time of this class the version is Aladin 7.5. 
 * So, if a new Aladin.jar version is deployed then the way the paths are built has to 
 * be changed.  
 * @author Julian Garrido
 * @date 10/04/2013
 *
 */
public class AladinInvoker {

	//private String script;
	private String std_out="";
	private String error_out="";
	private int option;
	
	public static final String GUI = "gui";
	public static final String NOGUI = "nogui";
	
	private String ALADINJAR = "/Applications/Aladin.app/Contents/Resources/Java/Aladin.jar";
	//private String Aladinjar = "/Users/julian/Documents/wf4ever/aladin/Aladin.jar";
	//private String ALADINJAR = "/home/julian/Documentos/wf4ever/aladin/Aladin.jar";
	
	private static Logger logger = Logger.getLogger(AladinInvoker.class);
	
	public AladinInvoker() throws IOException{
		initAladinJar();
	}
	
	public AladinInvoker(int opt ) throws IOException{
		option = opt;
		initAladinJar();
	}
		
	public void runScript(String script, String gui) throws InterruptedException, IOException{
		ProcessBuilder builder;
		if(AladinInvoker.GUI.compareTo(gui)!=0){
		    //System.out.println("java -jar " + ALADINJAR + " -nogui script="+script);
			//builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-nogui", "script="+script);
			builder = new ProcessBuilder("java", "-jar", ALADINJAR, "-nogui", "script="+script);
		}else{
			///Users/julian/Documents/wf4ever/aladin/
			//builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "script="+script);
			builder = new ProcessBuilder("java", "-jar", ALADINJAR, "script="+script);
		}
		
		//Map<String, String> environ = builder.environment();

	    Process process;
	    SecurityManager securityBackup = System.getSecurityManager();
		System.setSecurityManager(new NoExitSecurityManager_());
		
		try{
		    
			process = builder.start();
		    InputStream is = process.getInputStream();		    
		    StreamReaderAsync outputReader = new StreamReaderAsync(is, "OUTPUT");
		    
		    InputStream eis = process.getErrorStream();
		    StreamReaderAsync errorReader = new StreamReaderAsync(eis, "ERROR");
		    
		    //start the threads
		    outputReader.start();
		    errorReader.start();
	    
	    
		    //System.out.println("Estoy antes del waitfor");
		    int exitValue = process.waitFor();
		    //System.out.println("Estoy despues del waitfor");
		    
		    //is.close();
		    //eis.close();
		    	        
		    this.error_out = errorReader.getResult();
		    this.std_out = outputReader.getResult();
		   
		    //System.out.println("exit value for the process: " + process.exitValue());
		    process.destroy();
		    
		}catch(SecurityException ex){
			System.out.println("Se ha ejecutado exit() en AladinInvoker");
			logger.error("Se ha ejecutado exit() en AladinInvoker");
		}
		
		System.setSecurityManager(securityBackup);
	    
		
		    
	    //System.out.println("ERROR: " + this.error_out);
	    //System.out.println("STD: " + this.std_out);
		
		
	}
	
	public String getStd_out() {
		return std_out;
	}

	public String getError_out() {
		return error_out;
	}

	public void runScriptURL(String url, String gui) throws InterruptedException, IOException{
		ProcessBuilder builder;
		if(AladinInvoker.GUI.compareTo(gui)!=0){
		
			//ProcessBuilder builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-nogui", "-scriptfile="+url); 
			//builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-nogui", "-scriptfile="+url);
			builder = new ProcessBuilder("java", "-jar", ALADINJAR, "-nogui", "-scriptfile="+url);
		}else{
			///Users/julian/Documents/wf4ever/aladin/
			//builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-scriptfile="+url);
			builder = new ProcessBuilder("java", "-jar", ALADINJAR, "-scriptfile="+url);
		}
		
		////Map<String, String> environ = builder.environment();

	    Process process;

		process = builder.start();
	
	    InputStream is = process.getInputStream();		    
	    StreamReaderAsync outputReader = new StreamReaderAsync(is, "OUTPUT");
	    
	    InputStream eis = process.getErrorStream();
	    StreamReaderAsync errorReader = new StreamReaderAsync(eis, "ERROR");
	    
	    //start the threads
	    outputReader.start();
	    errorReader.start();
	    
	    int exitValue = process.waitFor();
	    
	    //is.close();
	    //eis.close();
	    
	    this.error_out = errorReader.getResult();
	    this.std_out = outputReader.getResult();
	    
		    		
	}
	
	/**
	 * Find the folder where Aladin.jar is and initialize ALADINJAR with the full path. 
	 * This is suppouse to be the version 7.5. If a new version is deployed, the way the path is built 
	 * has to be changed.
	 * @throws IOException 
	 */
	public void initAladinJar() throws IOException{
		File file = ClassLocation.getClassLocationFile(AnnotationAssertion.class);	
		String path = file.getAbsolutePath();
		int position = path.indexOf("repository");
		position = path.indexOf("net"+File.separator+"sf"+File.separator+"taverna"+File.separator+"t2"+File.separator+"core");
		ALADINJAR = path.substring(0, position);
		ALADINJAR += "cds"+File.separator+"aladin"+File.separator+"Aladin"+File.separator+"7.5"+File.separator+"Aladin-7.5.jar";
        //System.out.println(ALADINJAR);	
        logger.info("Aladin to be used at: "+ ALADINJAR);
	}
	
	
	public void runMacro(String scriptURL, String parametersURL, String gui) throws InterruptedException, IOException{
		
		String macroScript = "macro "+ scriptURL + " " + parametersURL; 
		System.out.println("Calling Aladin script: "+ macroScript);
		runScript(macroScript, gui);
		
	}
	

	protected void run() throws IOException{
		try {		
			if(option == 1){
				String example2 = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/exampleTests/m1.jpg; quit";
				System.out.println("Starting option 1");
				runScript(example2, "gui");				
				System.out.println("Ending option 1");
			}else if(option ==2){
				String scriptpath = "/Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/examplescript.ajs";
				String scriptURL = "file:///Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/examplescript.ajs";
				System.out.println("Starting option 2");
				runScriptURL(scriptpath, "nogui");
				System.out.println("Ending option 2");
			}else if(option == 3){
				String scriptMacro ="macro /Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_script.ajs /Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_params.txt";
				System.out.println("Starting option 3");
				runScript(scriptMacro, "nogui");
				System.out.println("Ending option 3");
			}else if(option == 4){
				System.out.println("Starting option 4");
				runMacro("/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_script.ajs", "/Users/julian/src/astrotaverna/Image-activity/src/test/resources/Aladin_workflow_params.txt", "nogui");
				System.out.println("Ending option 4");
			}else if(option == 5){
				String example2 = "get aladin(J,FITS) m1 ;\n save /home/julian/Documentos/wf4ever/aladin/m1.jpg; quit";
				System.out.println("Starting option 5");
				runScript(example2, "gui");				
				System.out.println("Ending option 5");
			}else if(option == 6){
				System.out.println("Starting option 6");
				runMacro("file:///home/julian/Documentos/wf4ever/aladin/Aladin_workflow_script.ajs", "file:///home/julian/Documentos/wf4ever/aladin/Aladin_workflow_params.txt", "nogui");
				System.out.println("Ending option 6");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		AladinInvoker invoker4 = new AladinInvoker(4);
		AladinInvoker invoker3 = new AladinInvoker(3);
		AladinInvoker invoker2 = new AladinInvoker(2);
		AladinInvoker invoker1 = new AladinInvoker(1);
		AladinInvoker invoker5 = new AladinInvoker(5);
		AladinInvoker invoker6 = new AladinInvoker(6);
		
		//invoker1.run();
		//invoker2.run();
		//invoker3.run();
		invoker4.run();
		//invoker5.run();
		//invoker6.run();
		System.out.println("The end");
	}

}
