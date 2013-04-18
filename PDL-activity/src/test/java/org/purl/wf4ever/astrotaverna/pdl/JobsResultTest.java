package org.purl.wf4ever.astrotaverna.pdl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class JobsResultTest {

	@Test
	public void readStringXML(){
		String serviceResult = 
				//"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> \n" +
				"<?xml version='1.0' ?>"+
				"<JobDetail>"+
				"    <JobId>9</JobId>"+
				"    <JobPhase>finished</JobPhase>"+
				"    <DemandDate>2013/04/18 11:50:31</DemandDate>"+
				"    <FinishingDate>2013/04/17 18:47:03</FinishingDate>"+
				"    <Inputs>"+
				"        <param>"+
				"            <Name>NH2Lev</Name>"+
				"            <Value>150</Value>"+
				"        </param>"+
				"        <param>"+
				"            <Name>Bbeta</Name>"+
				"            <Value>1.0</Value>"+
				"        </param>"+
				"        <param>"+
				"            <Name>NH2Lines</Name>"+
				"            <Value>200</Value>"+
				"        </param>"+
				"    </Inputs>"+
				"    <Outputs>"+
				"        <param>"+
				"            <Name>ParisDurhamFileResult</Name>"+
				"            <Value>http://pdl-calc.obspm.fr:8081/ParisDurham/output/9.tgz</Value>"+
				"        </param>"+
				"    </Outputs>"+
				"</JobDetail>";
		
		JobResult obj = new JobResult();

			try {
				obj.parseXML(serviceResult);
				//for(Entry<String, String> entry : obj.getInputParams().entrySet()){
				//	System.out.println("input: "+entry.getKey()+", "+entry.getValue());
				//}
				//for(Entry<String, String> entry : obj.getOutputParams().entrySet()){
				//	System.out.println("output: "+entry.getKey()+", "+entry.getValue());
				//}
				assertEquals(3, obj.getInputParams().size());
				assertEquals(1, obj.getOutputParams().size());
				assertEquals("finished", obj.getJobPhase());
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
