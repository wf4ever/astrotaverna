package org.purl.wf4ever.astrotaverna.image.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;



import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class ImageServiceProvider implements ServiceDescriptionProvider {
	
	//OJO!!!!!!!!!!!!!!!!!!!!
	//write down a real URI
	private static final URI providerId = URI
		.create("http://www.iaa.es/service-provider/tjoin");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		// FIXME: Implement the actual service search/lookup instead
		// of dummy for-loop
		//for (int i = 1; i <= 5; i++) {
		//	StiltsServiceDesc service = new StiltsServiceDesc();
		//	// Populate the service description bean
		//	service.setExampleString("Example " + i);
		//	service.setExampleUri(URI.create("http://localhost:8192/service"));

		//	// Optional: set description
		//	service.setDescription("Service example number " + i);
		//	results.add(service);
		//}
		
		AladinScriptServiceDesc service = new AladinScriptServiceDesc();
		service.setTypeOfInput("String");
		service.setTypeOfMode("nogui");
		//service.setDescription("Aladin script");
		
		results.add(service);

		AladinMacroServiceDesc service2 = new AladinMacroServiceDesc();
		service2.setTypeOfInput("String");
		service2.setTypeOfMode("nogui");
		//service.setDescription("Aladin macro");
		
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

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return ImageServiceIcon.getIcon();
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
