package org.purl.wf4ever.astrotaverna.aladin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AladinScriptParser {
	private String script;
	private String scriptURL;
	
	private ArrayList<String> files = new ArrayList<String>();
	
	public ArrayList<String> parseURL(String url) throws MalformedURLException, IOException{
		script = getFileContentFromUrl(url);
		parseScript(script);
		return files;
	}
	
	public ArrayList<String> parseFile(String file) throws IOException{
		script = readFileAsString(file);
		parseScript(script);
		return files;
	}
	
	public ArrayList<String> parseScript (String script){
		this.script = script;
		String lines[] = script.split("\\r?\\n");
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
	
	
	public String getScript() {
		return script;
	}

	private void setScript(String script) {
		this.script = script;		
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
	
	public String getVOTable(ArrayList<String> list){
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
				body = "  <TR><TD>"+item+"</TD></TR>\n";
		}else{
			body = "  <TR><TD></TD></TR>";
		}
		
		votable = header + body + footer;
		
		return votable;
	}
}
