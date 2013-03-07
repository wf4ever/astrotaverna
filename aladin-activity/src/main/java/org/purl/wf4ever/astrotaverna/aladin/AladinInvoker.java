package org.purl.wf4ever.astrotaverna.aladin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.purl.wf4ever.astrotaverna.utils.StreamReaderAsync;


public class AladinInvoker {

	private String script;
	private String std_out;
	private String error_out;
	private int option;
	
	public AladinInvoker(){
	
	}
	
	public AladinInvoker(int opt ){
		option = opt;
	}
		
	public void runScript(String script) throws InterruptedException, IOException{
		String[] args = new String[2];
		
		args[0]="-nogui";
		args[1] = "script="+script;
		
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-nogui", "script="+script); 
		
		Map<String, String> environ = builder.environment();

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
		    
		    this.error_out = errorReader.getResult();
		    this.std_out = outputReader.getResult();
		    

		
		
	}
	
	public String getStd_out() {
		return std_out;
	}

	public String getError_out() {
		return error_out;
	}

	public void runScriptURL(String url) throws InterruptedException, IOException{
		String[] args = new String[2];
		
		args[0]="-nogui";
		args[1] = "-scriptfile="+url;
		
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "/Users/julian/Documents/wf4ever/aladin/Aladin.jar", "-nogui", "-scriptfile="+url); 
		
		Map<String, String> environ = builder.environment();

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
		    
		    this.error_out = errorReader.getResult();
		    this.std_out = outputReader.getResult();
		    		
	}
	

	public void run() throws IOException{
		try {		
			if(option == 1){
				String example2 = "get aladin(J,FITS) m1 ;\n save /Users/julian/Documents/wf4ever/aladin/example & tests/m1.jpg; quit";
				System.out.println("Starting option 1");
				runScript(example2);				
				System.out.println("Ending option 1");
			}else if(option ==2){
				String scriptpath = "/Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/examplescript.ajs";
				String scriptURL = "file:///Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/examplescript.ajs";
				System.out.println("Starting option 2");
				runScriptURL(scriptpath);
				System.out.println("Ending option 2");
			}else if(option == 3){
				String scriptMacro ="macro /Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_script.ajs /Users/julian/workspaces/aladinTest_ws/myAladin/myTestSRC/iaa/amiga/aladin/resources/Aladin_workflow_params.txt";
				System.out.println("Starting option 3");
				runScript(scriptMacro);
				System.out.println("Ending option 3");
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

		
		AladinInvoker invoker3 = new AladinInvoker(3);
		AladinInvoker invoker2 = new AladinInvoker(2);
		AladinInvoker invoker1 = new AladinInvoker(1);
		
		invoker3.run();
		invoker2.run();
		invoker1.run();
		
		System.out.println("The end");
	}

}
