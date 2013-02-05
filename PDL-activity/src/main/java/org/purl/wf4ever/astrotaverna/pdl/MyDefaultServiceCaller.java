package org.purl.wf4ever.astrotaverna.pdl;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.utilities.Utilities;
//import net.ivoa.pdl.servicecaller.IserviceCaller;

public class MyDefaultServiceCaller {
	
	private static Logger logger = Logger.getLogger(MyDefaultServiceCaller.class);
	private String response;
	private String jobInfo;
	
	public String callService() {
		String serviceUrl = Utilities.getInstance().getService().getServiceId()
				+ "?";
		serviceUrl = serviceUrl.replaceAll("/OnlineCode?", "TavernaCodeFrontal?");
		//serviceUrl = "http://pdl-calc.obspm.fr:8081/montage/TavernaCodeFrontal?";
		
		List<SingleParameter> paramList = Utilities.getInstance().getService()
				.getParameters().getParameter();
		
		for (int i = 0; i < paramList.size(); i++) {
			try{
			
				SingleParameter p = paramList.get(i);
				String character = "";
				if (i > 0) {
					character = "&";
				}
				serviceUrl = serviceUrl
						+ character
						+ p.getName()
						+ "="
						+ Utilities.getInstance()
								.getuserProvidedValuesForParameter(p).get(0)
								.getValue();
			}catch (Exception e) {
				// TODO: do nothing
			}
		}
		System.out.println("Get result from: " + serviceUrl);
		logger.info("Get result from: " + serviceUrl);
		try {
			
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
		} catch (Exception e) {
			e.printStackTrace();
			response = "errors";
			return response;
		}
	}
	
	public String getJobInfo(String jobId, String userId) {
		String serviceUrl = Utilities.getInstance().getService().getServiceId()
				+ "?";
		serviceUrl = serviceUrl.replaceAll("/OnlineCode?", "TavernaJobInfo?");
		//serviceUrl = "http://pdl-calc.obspm.fr:8081/montage/TavernaCodeFrontal?";
		
		
		List<SingleParameter> paramList = Utilities.getInstance().getService()
				.getParameters().getParameter();
		
		for (int i = 0; i < paramList.size(); i++) {
			try{
			
			SingleParameter p = paramList.get(i);
			String character = "";
			if (i > 0) {
				character = "&";
			}
			serviceUrl = serviceUrl
					+ character
					+ p.getName()
					+ "="
					+ Utilities.getInstance()
							.getuserProvidedValuesForParameter(p).get(0)
							.getValue();
			}catch (Exception e) {
				// TODO: do nothing
			}
		}
		if(paramList.size()>0)
			serviceUrl = serviceUrl + "&jobId="+jobId+"&userId="+userId;
		System.out.println("Get result from: " + serviceUrl);
		logger.info("Get result from: " + serviceUrl);
		try {
			
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
		} catch (Exception e) {
			e.printStackTrace();
			jobInfo = "errors";
			return jobInfo;
		}
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

}
