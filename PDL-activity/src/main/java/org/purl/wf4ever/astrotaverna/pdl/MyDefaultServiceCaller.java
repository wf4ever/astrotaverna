package org.purl.wf4ever.astrotaverna.pdl;

import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import CommonsObjects.GeneralParameter;


import net.ivoa.parameter.model.ParameterType;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.utilities.Utilities;
//import net.ivoa.pdl.servicecaller.IserviceCaller;

public class MyDefaultServiceCaller {
	
	private static Logger logger = Logger.getLogger(MyDefaultServiceCaller.class);
	private String response;
	private String jobInfo;
	private String serviceUrl;
	
	public String callService(HashMap<String, SingleParameter> paramMap) throws MalformedURLException, IOException {
		serviceUrl = Utilities.getInstance().getService().getServiceId()
				+ "?";
		serviceUrl = serviceUrl.replaceAll("/OnlineCode", "/TavernaCodeFrontal");

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
		
		System.out.println("Call service: " + serviceUrl);
		logger.info("Call Service: " + serviceUrl);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new URL(serviceUrl).openConnection()
						.getInputStream()));
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		bufferedReader.close();
		response = sb.toString();
		return response;

	}
	
	public String getJobInfo(String jobId, String userId) throws MalformedURLException, IOException {
		serviceUrl = Utilities.getInstance().getService().getServiceId()
				+ "?";
		
		serviceUrl = serviceUrl.replaceAll("/OnlineCode", "/TavernaJobInfo");
		//serviceUrl = "http://pdl-calc.obspm.fr:8081/montage/TavernaCodeFrontal?";
		
		
		
		String mail = Utilities.getInstance().getuserProvidedValuesForParame("mail").get(0).getValue();
		
		if(mail!=null){
			serviceUrl = serviceUrl + "mail="+mail+ "&jobId="+jobId+"&userId="+userId;
		}else{
			serviceUrl = serviceUrl + "jobId="+jobId+"&userId="+userId;
		}

		System.out.println("Get result from: " + serviceUrl);
			
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new URL(serviceUrl).openConnection()
						.getInputStream()));
		
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		bufferedReader.close();
		jobInfo = sb.toString();
		return jobInfo;

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
