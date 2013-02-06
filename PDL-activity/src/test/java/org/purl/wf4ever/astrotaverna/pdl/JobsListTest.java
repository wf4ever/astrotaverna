package org.purl.wf4ever.astrotaverna.pdl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class JobsListTest {

	@Test
	public void readStringXML(){
		String serviceResult = 
				//"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> \n" +
				"<?xml version='1.0' ?>"+
				"<JobsList> " +
				"    <ServiceName>http://pdl-calc.obspm.fr:8081/montage/</ServiceName> " +
				"    <List> " +
				"        <JobId>3</JobId> " +
				"        <UserId>7</UserId> " +
				"    </List> " +
				"</JobsList>";
		JobsList obj = new JobsList();

			try {
				obj.parseXML(serviceResult);
				assertEquals("3", obj.getJobs().get(0).getJobId());
				assertEquals("7", obj.getJobs().get(0).getUserId());
				//System.out.println("job: "+obj.getJobs().get(0).getJobId()+" - user: "+obj.getJobs().get(0).getUserId());
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
