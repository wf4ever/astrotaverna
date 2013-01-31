package org.purl.wf4ever.astrotaverna.pdl;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.utilities.Utilities;
//import net.ivoa.pdl.servicecaller.IserviceCaller;

public class MyDefaultServiceCaller {
	public String callService() {
		String serviceUrl = Utilities.getInstance().getService().getServiceId()
				+ "?";
		
		
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
		System.out.println(serviceUrl);
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
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "errors";
		}
	}

}
