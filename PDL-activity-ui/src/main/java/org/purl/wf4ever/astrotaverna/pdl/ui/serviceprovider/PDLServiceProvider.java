package org.purl.wf4ever.astrotaverna.pdl.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.purl.wf4ever.astrotaverna.pdl.ui.serviceprovider.ValidationPDLClientServiceDesc;


import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class PDLServiceProvider implements ServiceDescriptionProvider {
	
	//OJO!!!!!!!!!!!!!!!!!!!!
	//write down a real URI
	private static final URI providerId = URI
		.create("http://www.iaa.es/service-provider/pdl");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();
	
		
		ValidationPDLClientServiceDesc service1 = new ValidationPDLClientServiceDesc();
		service1.setPdlDescriptionFile("http://www.exampleuri.com/pdldescriptionfile.xml");
		service1.setDescription("Validate inputs with pdl-description");
		results.add(service1);
		
		PDLService_ServiceDesc service2 = new PDLService_ServiceDesc();
		service2.setPdlDescriptionFile("http://www.exampleuri.com/pdldescriptionfile.xml");
		service2.setDescription("Import PDL service (Beta)");
		results.add(service2);
		
		//Put here additional descriptions for other services
		//............
		//............
		//............
		//............
		

		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);

		// No more results will be coming
		callBack.finished();
	}
	/*
	@SuppressWarnings("unchecked")
	public static ServiceDescription getPDLService_ServiceDescription() {
		PDLService_ServiceDesc service2 = new PDLService_ServiceDesc();
		service2.setPdlDescriptionFile("http://www.exampleuri.com/pdldescriptionfile.xml");
		service2.setDescription("Import PDL service (Beta)");
		return service2;
	}
	*/

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return PDLServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "My astro services";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
