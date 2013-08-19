package org.purl.wf4ever.astrotaverna.aladin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AladinScriptParser {
	private String script;
	private String scriptURL;
	private String params;
	private String paramsURL;
	
	private ArrayList<String> files = new ArrayList<String>();
	
	public ArrayList<String> parseURL(String url) throws MalformedURLException, IOException{
		script = getFileContentFromUrl(url);
		parseScript(script);
		return files;
	}
	
	public ArrayList<String> parseFile(String file) throws IOException{
		//script = readFileAsString(file);
		script = readTextFile(file,null);
		parseScript(script);
		return files;
	}
	
	public ArrayList<String> parseScript (String script){
		this.script = script;
		String newline = System.getProperty("line.separator");
		//String lines[] = script.split("\\r?\\n");
		String lines[] = script.split(newline);
		files = new ArrayList<String>();
		if(lines!=null)
			for(String line : lines){
				//split by ;
				String commands[] = line.split(";");
				if(commands!=null)
					for(String command : commands){
						
						//is it a comment?
						command = command.trim();
						//String fileExpression="(([a-zA-Z]+:)?[[a-zA-Z0-9_-]+|"+Pattern.quote("[.&\\/ %]*") +"]+)";
						String fileExpression="(([a-zA-Z]+:)?\\\\?[[a-zA-Z0-9_-]+|"+Pattern.quote("[.&$\\/ %]*") +"]+)";
						//String expression =".*save\\s+[-png|-PNG|-jpg|-JPG|-eps|-EPS|-l*k*]*\\s+"+fileExpression;
						//fileExpression = "(.*)";
						//String flags = Pattern.quote("(-png|-PNG|-jpg|-JPG|-eps|-EPS|-l*k*)*");
						String expression ="";
						
						if(!command.startsWith("#")){
							//save [-eps|-jpg[NN]|-png] [-lk] [WxH] [filename]
							//Example:
							//	save m1.eps
							//	save -lk home\img.png
							//	save -jpg97 300x300
							
							//expression = "\\s*save\\s+"+fileExpression;
							expression = "\\s*save"+fileExpression;
							Pattern pattern = Pattern.compile(expression);
							Matcher matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									
									filename = filename.replaceAll("\\s+(-png)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-jpg)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-eps)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-EPS)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+-([l|k|L|K]{0,2})[\\s|^a-zA-Z-]*", " ");
									//filename = filename.replaceAll("[^a-zA-Z]*((-png)|(-jpg))[^a-zA-Z]*", " ");
									files.add(filename.trim());
								}
							}
							
							expression = "\\s*backup"+fileExpression;
							pattern = Pattern.compile(expression);
							matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									files.add(filename.trim());
								}
							}
							
							//export [-fmt] x filename
							//export -ROI filename
							//Example:
							//	export DSS1.V.SERC C:\DSS2image.fits
							//	export -votable GSC1.2 /home/gsc1.2.dat
							//	export RGBimg m1RGB.fits
							expression = "\\s*export(.*)";//+fileExpression;
							pattern = Pattern.compile(expression);
							matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									/*
									if(filename.matches("\\s+(RGBimg)[^a-zA-Z/\\-]*")){
										filename = filename.replaceAll("\\s+(RGBimg)[^a-zA-Z/\\-]*", " ");
									}
									if(filename.matches("\\s+(-votable)[^a-zA-Z/\\-]*")){
										filename.
										filename = filename.replaceAll("\\s+(-votable)[^a-zA-Z/\\-]*", " ");
									}
									*/
									String [] items = filename.split("\\s");
									if(items!=null && items.length>0)
									files.add(items[items.length-1].trim());
								}
							}
							
							
							//backup filename
							//Example:
							//	backup /home/M1.aj
						}
					}
			}
		
		return files;
	}
	

	public ArrayList<ArrayList<String>> parseMacro (String script, String params){
		this.script = script;
		String newline = System.getProperty("line.separator");
		//String lines[] = script.split("\\r?\\n");
		String lines[] = script.split(newline);
		files = new ArrayList<String>();
		ArrayList<ArrayList<String>> result;
		if(lines!=null)
			for(String line : lines){
				//split by ;
				String commands[] = line.split(";");
				if(commands!=null)
					for(String command : commands){
						
						//is it a comment?
						command = command.trim();
						//String fileExpression="(([a-zA-Z]+:)?[[a-zA-Z0-9_-]+|"+Pattern.quote("[.&\\/ %]*") +"]+)";
						String fileExpression="(([a-zA-Z]+:)?\\\\?[[a-zA-Z0-9_-]+|"+Pattern.quote("[.&$\\/ %]*") +"]+)";
						//String expression =".*save\\s+[-png|-PNG|-jpg|-JPG|-eps|-EPS|-l*k*]*\\s+"+fileExpression;
						//fileExpression = "(.*)";
						//String flags = Pattern.quote("(-png|-PNG|-jpg|-JPG|-eps|-EPS|-l*k*)*");
						String expression ="";
						
						if(!command.startsWith("#")){
							//save [-eps|-jpg[NN]|-png] [-lk] [WxH] [filename]
							//Example:
							//	save m1.eps
							//	save -lk home\img.png
							//	save -jpg97 300x300
							
							//expression = "\\s*save\\s+"+fileExpression;
							expression = "\\s*save"+fileExpression;
							Pattern pattern = Pattern.compile(expression);
							Matcher matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									
									filename = filename.replaceAll("\\s+(-png)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-jpg)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-eps)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+(-EPS)[^a-zA-Z/\\-]*", " ");
									filename = filename.replaceAll("\\s+-([l|k|L|K]{0,2})[\\s|^a-zA-Z-]*", " ");
									//filename = filename.replaceAll("[^a-zA-Z]*((-png)|(-jpg))[^a-zA-Z]*", " ");
									files.add(filename.trim());
								}
							}
							
							expression = "\\s*backup"+fileExpression;
							pattern = Pattern.compile(expression);
							matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									files.add(filename.trim());
								}
							}
							
							//export [-fmt] x filename
							//export -ROI filename
							//Example:
							//	export DSS1.V.SERC C:\DSS2image.fits
							//	export -votable GSC1.2 /home/gsc1.2.dat
							//	export RGBimg m1RGB.fits
							expression = "\\s*export(.*)";//+fileExpression;
							pattern = Pattern.compile(expression);
							matcher = pattern.matcher(command);
							while(matcher.find()){
								String filename = matcher.group(1);
								if(filename!=null && filename.length()>0){
									/*
									if(filename.matches("\\s+(RGBimg)[^a-zA-Z/\\-]*")){
										filename = filename.replaceAll("\\s+(RGBimg)[^a-zA-Z/\\-]*", " ");
									}
									if(filename.matches("\\s+(-votable)[^a-zA-Z/\\-]*")){
										filename.
										filename = filename.replaceAll("\\s+(-votable)[^a-zA-Z/\\-]*", " ");
									}
									*/
									String [] items = filename.split("\\s");
									if(items!=null && items.length>0)
									files.add(items[items.length-1].trim());
								}
							}
							
							
							//backup filename
							//Example:
							//	backup /home/M1.aj
						}
					}
			}
		
		ArrayList<ArrayList<String>> paramsLists = parseParams(params);
		
		result = replaceDolarsByValues(files, paramsLists);
		//System.out.println(files);
		return result;
	}
	
	
	public ArrayList<ArrayList<String>> parseURLMacro(String url, String url_params) throws MalformedURLException, IOException{
		script = getFileContentFromUrl(url);
		params = getFileContentFromUrl(url_params);
		ArrayList<ArrayList<String>> result = parseMacro(script, params);
		return result;
	}
	
	public ArrayList<ArrayList<String>> parseFileMacro(String file, String file_params) throws IOException{
		script = readTextFile(file,"utf8");
		params = readTextFile(file_params,"utf8");
		ArrayList<ArrayList<String>> result = parseMacro(script, params);
		return result;
	}
	
	/**
	 * Get a list o param list from a tab separated values file. 
	 * @param params
	 * @return
	 */
	ArrayList<ArrayList<String>> parseParams(String params){
		ArrayList<ArrayList<String>> paramsLists = new ArrayList<ArrayList<String>>();
				
		String[] lines = params.split(System.getProperty("line.separator"));
		
		for(String line: lines){
			ArrayList<String> list = new ArrayList<String>();
			String[] items = line.split("\t");
			for(String item : items){
				list.add(item);
			}
			paramsLists.add(list);
		}
		
		return paramsLists;
	}
	
	/**
	 * Replace $x in files by its corresponding value in values array. 
	 * $1 correspond with column 0, $2 with column 1, ...
	 * @param files
	 * @param values
	 * @return
	 */
	public ArrayList<ArrayList<String>> replaceDolarsByValues(ArrayList<String> files, ArrayList<ArrayList<String>> values){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		ArrayList<String> extendedFiles = new ArrayList<String>();
		
		if(files!=null && files.size()>0 && values!=null && values.size()>0)
			if(values.get(0)!=null && values.get(0).size()>0){
				int size = values.get(0).size();
				String [] dolars = new String[size];
				
				for(int i = 0; i<size; i++)
					dolars[i]=Pattern.quote("$")+(i+1);
				
				for(ArrayList<String> list : values){
					extendedFiles = new ArrayList<String>();
					for(String file: files){
						for(int i = 0; i<dolars.length; i++){
							String dolar = dolars[i];
							//Next commented line was the one reponsible for loosing the 
							//file = file.replaceAll(dolar, list.get(i));
							//Solution:
							file = file.replaceAll(dolar, Matcher.quoteReplacement(list.get(i)));
						}
						extendedFiles.add(file);
					}
					result.add(extendedFiles);
				}
				
			}
		
		return result;
	}
	
	//this code doesn't consider /t
	//this code is not tested
	ArrayList<ArrayList<String>> parseParamsWithTokenizer(String params){
		ArrayList<ArrayList<String>> paramsLists = new ArrayList<ArrayList<String>>();
		StreamTokenizer streamTokenizer = null;
		ArrayList<String> list = new ArrayList<String>();
		
		StringReader reader = new StringReader(params);
		
		try {
            
            streamTokenizer = new StreamTokenizer(reader);
            
            while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            	if (streamTokenizer.ttype != StreamTokenizer.TT_EOL){
            		if(streamTokenizer.ttype == streamTokenizer.TT_WORD){
	            		//System.out.println(streamTokenizer.sval+" --");
	            		list.add(streamTokenizer.sval);
            		}
            		if(streamTokenizer.ttype == streamTokenizer.TT_NUMBER){
	            		//System.out.println(streamTokenizer.nval+" ++");
	            		list.add(String.valueOf(streamTokenizer.nval));
            		}
            	}else{
            		//System.out.println("New line");
            		paramsLists.add(list);
            		list = new ArrayList<String>();
            	}
                    
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
		return paramsLists;
	}

	
	public String getScript() {
		return script;
	}

	private void setScript(String script) {
		this.script = script;		
	}

	BufferedReader getReader (String fileUrl, String encoding) throws IOException {
		InputStreamReader reader;
		try {
			if (encoding == null) {
				reader = new FileReader(fileUrl);
			} else {
				reader = new InputStreamReader(new FileInputStream(fileUrl),encoding); 
			}
		}
		catch (FileNotFoundException e) {
			// try a real URL instead
			URL url = new URL(fileUrl);
			if (encoding == null) {
				reader = new InputStreamReader (url.openStream());
			} else {
				reader = new InputStreamReader (url.openStream(), encoding);
			}
		}
		return new BufferedReader(reader);
	}
	
	private String readTextFile(String fileUrl, String encoding) throws IOException{

		StringBuffer sb = new StringBuffer(4000);
		
		BufferedReader in = getReader(fileUrl, encoding);
		String str;
		String lineEnding = System.getProperty("line.separator");

		while ((str = in.readLine()) != null) {
			sb.append(str);
			sb.append(lineEnding);
		}
		in.close();
		String filecontents = sb.toString();
		
		return filecontents;
	}
	
	private String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
	
	private String getFileContentFromUrl(String fileUrl) throws MalformedURLException, IOException {
		BufferedReader bufferedReader = new BufferedReader(
		new InputStreamReader(new URL(fileUrl).openConnection().getInputStream()));
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		bufferedReader.close();
		return sb.toString();
	}
	
	/**
	 * The method returns a VOTable with only one column
	 * @param list
	 * @return
	 */
	public String getOneColumnVOTable(ArrayList<String> list){
		String votable="";
		String header = "<?xml version='1.0'?>"
				+ "<VOTABLE version=\"1.1\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
				+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
				+ "<!--"
				+ " !  VOTable written by Astrotaverna"
				+ " !-->"
				+ "<RESOURCE>"
				+ "<TABLE nrows=\""+list.size()+"\">"
				+ "<DESCRIPTION>"
				+ "Result "
				+ "</DESCRIPTION>"
				+ "<FIELD arraysize=\"*\" datatype=\"char\" name=\"name\" ucd=\"ID_MAIN\">"
				+ "<DESCRIPTION>File results from aladin script</DESCRIPTION>"
				+ "</FIELD>"
				+ "<DATA>"
				+ "<TABLEDATA>";
		String footer =  "</TABLEDATA>"
				+ "</DATA>"
				+ "</TABLE>"
				+ "</RESOURCE>"
				+ "</VOTABLE>";
		String body ="";
		if(list.size()>0){
			for (String item : list)
				body += "  <TR><TD>"+item+"</TD></TR>\n";
		}else{
			body = "  <TR><TD></TD></TR>";
		}
		
		votable = header + body + footer;
		
		return votable;
	}
	
	/**
	 * The method returns a VOTable with only one column
	 * @param list
	 * @return
	 */
	public String getMultiColumnVOTable(ArrayList<ArrayList<String>> list){
		String votable="";
		String body ="";
		int numCols;
		String fields ="";
		
		//column headers and body
		if(!list.isEmpty()){
			numCols = list.get(0).size();
			for (int i = 0; i < numCols; i++){
				fields += "<FIELD arraysize=\"*\" datatype=\"char\" name=\"col"+(i+1)+"\">\n"
						+ "  <DESCRIPTION>results from aladin script</DESCRIPTION>\n"
						+ "</FIELD>\n";  
			}
			for (ArrayList<String> row : list)
				if(!row.isEmpty()){
					body += "  <TR>";
					for(String item: row)
						body += "<TD>"+item+"</TD>";
					body += "</TR>\n";
				}
		}else{
			numCols = 1;
			fields = "<FIELD arraysize=\"*\" datatype=\"char\" name=\"col1\">\n"
			+ "  <DESCRIPTION>File results from aladin script</DESCRIPTION>\n"
			+ "</FIELD>\n";
			
			body = "  <TR><TD></TD></TR>";
		}
		
		String header = "<?xml version='1.0'?>"
				+ "<VOTABLE version=\"1.1\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xsi:schemaLocation=\"http://www.ivoa.net/xml/VOTable/v1.1 http://www.ivoa.net/xml/VOTable/v1.1\""
				+ " xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">"
				+ "<!--"
				+ " !  VOTable written by Astrotaverna"
				+ " !-->"
				+ "<RESOURCE>"
				+ "<TABLE nrows=\""+list.size()+"\">"
				+ "<DESCRIPTION>"
				+ "Result "
				+ "</DESCRIPTION>"
				+ fields
				+ "<DATA>"
				+ "<TABLEDATA>";
		
		String footer =  "</TABLEDATA>"
				+ "</DATA>"
				+ "</TABLE>"
				+ "</RESOURCE>"
				+ "</VOTABLE>";
		
		
		
		
		votable = header + body + footer;
		
		return votable;
	}
	
	
}
