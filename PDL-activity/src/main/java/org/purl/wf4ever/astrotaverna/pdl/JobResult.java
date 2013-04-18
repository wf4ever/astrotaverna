package org.purl.wf4ever.astrotaverna.pdl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.dom4j.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JobResult {

	String jobPhase="unknown";
	String demandDate;
	
	HashMap<String, String> inputParams= new HashMap<String, String>();
	HashMap<String, String> outputParams= new HashMap<String, String>();
	
	public void parseXML(String xml) throws ParserConfigurationException, SAXException, IOException, IOException{
		
		inputParams = new HashMap<String, String>();
		outputParams = new HashMap<String, String>();
		

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //Using factory get an instance of document builder

        dbf.setNamespaceAware(false);
        DocumentBuilder builder = dbf.newDocumentBuilder();

        InputSource inStream = new InputSource();

        inStream.setCharacterStream(new StringReader(xml));
        Document dom = builder.parse(inStream);


        //get the root element
        Element docEle = dom.getDocumentElement();
        
        //get jobId
        NodeList nodeList =  docEle.getElementsByTagName("JobId");
        if(nodeList != null && nodeList.getLength()>0)
        	this.jobId = nodeList.item(0).getTextContent();
        
        //get JobPhase
        nodeList =  docEle.getElementsByTagName("JobPhase");
        if(nodeList != null && nodeList.getLength()>0)
        	this.jobPhase = nodeList.item(0).getTextContent();
        //get demand date
        nodeList =  docEle.getElementsByTagName("DemandDate");
        if(nodeList != null && nodeList.getLength()>0)
        	this.demandDate = nodeList.item(0).getTextContent();
        
        nodeList = docEle.getElementsByTagName("Inputs");
        if(nodeList!=null && nodeList.getLength() > 0 && nodeList.item(0).getNodeType() == Node.ELEMENT_NODE){
        	Element inputElement = (Element) nodeList.item(0);
        	nodeList = inputElement.getElementsByTagName("param");
        	for(int i = 0; i< nodeList.getLength(); i++){
        		if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
        			Element paramElement = (Element) nodeList.item(i);
        			NodeList nameList = paramElement.getElementsByTagName("Name");
        			String name = nameList.item(0).getTextContent();
        			NodeList valueList = paramElement.getElementsByTagName("Value");
        			String value = valueList.item(0).getTextContent();   			
        			this.inputParams.put(name, value);
        		}
        	}
        }	
        nodeList = docEle.getElementsByTagName("Outputs");
        if(nodeList!=null && nodeList.getLength() > 0 && nodeList.item(0).getNodeType() == Node.ELEMENT_NODE){
        	Element inputElement = (Element) nodeList.item(0);
        	nodeList = inputElement.getElementsByTagName("param");
        	for(int i = 0; i< nodeList.getLength(); i++){
        		if(nodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
        			Element paramElement = (Element) nodeList.item(i);
        			NodeList nameList = paramElement.getElementsByTagName("Name");
        			String name = nameList.item(0).getTextContent();
        			NodeList valueList = paramElement.getElementsByTagName("Value");
        			String value = valueList.item(0).getTextContent();   			
        			this.outputParams.put(name, value);
        		}
        	}
        }	
	}
	
	

	String jobId;
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobPhase() {
		return jobPhase;
	}

	public void setJobPhase(String jobPhase) {
		this.jobPhase = jobPhase;
	}

	public String getDemandDate() {
		return demandDate;
	}

	public void setDemandDate(String demandDate) {
		this.demandDate = demandDate;
	}

	public HashMap<String, String> getInputParams() {
		return inputParams;
	}

	public void setInputParams(HashMap<String, String> inputParams) {
		this.inputParams = inputParams;
	}

	public HashMap<String, String> getOutputParams() {
		return outputParams;
	}

	public void setOutputParams(HashMap<String, String> outputParams) {
		this.outputParams = outputParams;
	}
	
}
