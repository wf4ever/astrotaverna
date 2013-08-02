package org.purl.wf4ever.astrotaverna.pdl;

import java.util.HashMap;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;

import CommonsObjects.GeneralParameter;


import net.ivoa.parameter.model.ParameterType;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.utilities.Utilities;
//import net.ivoa.pdl.servicecaller.IserviceCaller;

/**
 * This class is able to invoke a Rest service using the information in the PDL description. 
 * It needs the value of each parameter that is used to build the url.
 * @author Julian Garrido
 *
 */
public class RestServiceCaller {
	
	private static Logger logger = Logger.getLogger(RestServiceCaller.class);
	private String response;
	private StarTable table;
	private String jobInfo;
	private String serviceUrl;
	
	public String callService(HashMap<String, SingleParameter> paramMap) throws MalformedURLException, IOException {
		serviceUrl = Utilities.getInstance().getService().getServiceId() + "?";

		String paramName="";
		boolean firstParam = true;
		System.out.println();
		for(String key : paramMap.keySet()){
			try{
				SingleParameter p = paramMap.get(key);
				//SingleParameter p = paramList.get(i);
				String character = "";
				if (!firstParam) {
					character = "&";
				}

				paramName = p.getName();
				//System.out.print("name: "+ paramName);
				List<GeneralParameter> gplist = Utilities.getInstance().getuserProvidedValuesForParameter(p);
				//if(gplist.size()>0)
				//	System.out.println();
				GeneralParameter gn = gplist.get(0);
                String paramValue = Utilities.getInstance().getuserProvidedValuesForParameter(p).get(0).getValue();
                //System.out.println(" -- value: " + paramValue);
                if (p.getParameterType().equals(ParameterType.STRING)) {
                        paramValue = URLEncoder.encode(paramValue, "UTF-8");
                }

                serviceUrl = serviceUrl + character + paramName + "="
                                + paramValue;
                
                if(firstParam){
                	firstParam=false;
                }
                
			}catch (Exception e) {
				System.out.println("Unexpected exception building the url for the parameter "+paramName);
				logger.info("Unexpected exception building the url: " + serviceUrl);
				e.printStackTrace();
			}
		}
		
		System.out.println("(Astrotaverna) Call service: " + serviceUrl);
		logger.info("(Astrotaverna) Call Service: " + serviceUrl);

		response = getURLContent(serviceUrl);
		
		return response;

	}
	
	/**
	 * It builds the URL (using paramMap) to call a service. The service must return 
	 * a votable. 
	 * @param paramMap
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public StarTable callServiceReturningVOTable(HashMap<String, SingleParameter> paramMap) throws MalformedURLException, IOException {
		serviceUrl = Utilities.getInstance().getService().getServiceId() + "?";

		String paramName="";
		boolean firstParam = true;
		System.out.println();
		for(String key : paramMap.keySet()){
			try{
				SingleParameter p = paramMap.get(key);
				//SingleParameter p = paramList.get(i);
				String character = "";
				if (!firstParam) {
					character = "&";
				}

				paramName = p.getName();
				//System.out.print("name: "+ paramName);
				List<GeneralParameter> gplist = Utilities.getInstance().getuserProvidedValuesForParameter(p);
				//if(gplist.size()>0)
				//	System.out.println();
				GeneralParameter gn = gplist.get(0);
                String paramValue = Utilities.getInstance().getuserProvidedValuesForParameter(p).get(0).getValue();
                //System.out.println(" -- value: " + paramValue);
                if (p.getParameterType().equals(ParameterType.STRING)) {
                        paramValue = URLEncoder.encode(paramValue, "UTF-8");
                }

                serviceUrl = serviceUrl + character + paramName + "="
                                + paramValue;
                
                if(firstParam){
                	firstParam=false;
                }
                
			}catch (Exception e) {
				System.out.println("Unexpected exception building the url for the parameter "+paramName);
				logger.info("Unexpected exception building the url: " + serviceUrl);
				e.printStackTrace();
			}
		}
		
		System.out.println("(Astrotaverna) Call service: " + serviceUrl);
		logger.info("(Astrotaverna) Call Service: " + serviceUrl);

		table = new StarTableFactory().makeStarTable(serviceUrl, "votable" );
		
		return table;

	}
	
	/**
	 * It retrieves the content from a url condidering UTF-8 codification
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getURLContent(String url) throws MalformedURLException, IOException{
		InputStream inputStream = new URL(url).openConnection().getInputStream();
		String result = IOUtils.toString(inputStream, "UTF-8");
		return result;
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
	
	public String latestInvokedURL(){
		return serviceUrl;
	}

}
