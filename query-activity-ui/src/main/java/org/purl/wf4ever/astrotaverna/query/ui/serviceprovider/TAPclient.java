package org.purl.wf4ever.astrotaverna.query.ui.serviceprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;


/**
 * TapClien class that is able to call a tap service. By the time being, it
 * only implements doQuery request.  
 * @author julian Garrido
 *
 */
public class TAPclient {

	public static final String ADQL = "ADQL";
	public static final String PQL = "PQL";
	public static final String DOQUERY = "doQuery";
	
	private String endPoint;
	private String lang; 
	private int max_rows;
	private boolean sync;
	private String query;
	private String request;
	
	
	/**
	 * TAP Client Constructor
	 * @param tapEndPoint
	 * @param lang
	 * @param sync
	 */
	public TAPclient(String tapEndPoint, String lang, boolean sync, String query){
		request = DOQUERY;
		this.endPoint = tapEndPoint;
		this.lang = lang;
		this.sync = sync;
		this.query = query;
	}

	/**
	 * build the URL 
	 * @return
	 */
	public URL getTapURL(){
		URL url=null;
		String cad = endPoint; 
		
		
		
		return url;
	}
	
	public String getEndPoint() {
		return endPoint;
	}


	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}


	public String getLang() {
		return lang;
	}


	public void setLang(String lang) {
		this.lang = lang;
	}


	public int getMax_rows() {
		return max_rows;
	}


	public void setMax_rows(int max_rows) {
		this.max_rows = max_rows;
	}


	public boolean isSync() {
		return sync;
	}


	public void setSync(boolean sync) {
		this.sync = sync;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getRequest() {
		return request;
	}
	
	


}
