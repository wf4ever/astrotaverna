package org.purl.wf4ever.astrotaverna.pdl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JobsList {

	String serviceName;
	ArrayList<Job> jobs;
	
	public void parseXML(String xml) throws ParserConfigurationException, SAXException, IOException, IOException{
		String jobid = "";
		String userid = "";
		
		
		jobs = new ArrayList<Job>();
		
		//try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //Using factory get an instance of document builder

             dbf.setNamespaceAware(false);
             DocumentBuilder builder = dbf.newDocumentBuilder();

             InputSource inStream = new InputSource();

             inStream.setCharacterStream(new StringReader(xml));
             Document dom = builder.parse(inStream);


            //get the root element
            Element docEle = dom.getDocumentElement();
            
            //get service name
            NodeList service =  docEle.getElementsByTagName("ServiceName");
            if(service != null && service.getLength()>0)
            	this.serviceName = service.item(0).getTextContent();
                       
            //get list elements
            NodeList nl = docEle.getElementsByTagName("List");

            if(nl != null && nl.getLength() > 0) {
            	//there should be only one job
            	
            	Element el = (Element)nl.item(0);
            	NodeList job = el.getElementsByTagName("JobId");
            	if(job != null && job.getLength()>0)
            		jobid = job.item(0).getTextContent();

            	NodeList user = el.getElementsByTagName("UserId");
            	if(user != null && user.getLength()>0)
            		userid = user.item(0).getTextContent();
            	
            	jobs.add(new Job(jobid, userid));
            }
            

/*
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }*/
		
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}


	public class Job{
		private String jobId;
		private String userId;
		
		public String getJobId() {
			return jobId;
		}

		public void setJobId(String jobId) {
			this.jobId = jobId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public Job(String jobId, String userId){
			this.jobId = jobId;
			this.userId = userId;
		}
	}
}
