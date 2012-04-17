package uk.org.taverna.astro.vorepo;

import java.net.MalformedURLException;
import java.net.URI;

import javax.xml.ws.BindingProvider;

import net.ivoa.wsdl.registryinterface.v1_0.RegistryInterface;
import net.ivoa.wsdl.registrysearch.v1.KeywordSearch;
import net.ivoa.wsdl.registrysearch.v1.SearchResponse;
import net.ivoa.wsdl.registrysearch.v1_0.ErrorResp;
import net.ivoa.wsdl.registrysearch.v1_0.RegistrySearchPortType;

public class VORepository {

	private static final String REGISTRY_SEARCH_SERVICE = "RegistrySearchService";

	public enum Status {
		OK, ERROR, CONNECTION_ERROR, CONNECTION_TIMEOUT,
	}

	public static final URI DEFAULT_ENDPOINT = URI
			.create("http://registry.euro-vo.org/services/RegistrySearch");

	public static final URI WSDL = URI
			.create("http://www.ivoa.net/wsdl/RegistrySearch/v1.0");

	private URI endpoint;

	public VORepository(URI endpoint) {
		setEndpoint(endpoint);
	}

	public VORepository() {
		setEndpoint(null);
	}

	public URI getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(URI endpoint) {
		if (endpoint == null) {
			endpoint = DEFAULT_ENDPOINT;
		}
		this.endpoint = endpoint;
	}

	public Status getStatus() throws MalformedURLException, ErrorResp {
		RegistrySearchPortType port = getPort();
		
		KeywordSearch keywordSearch = new KeywordSearch();
		keywordSearch.setKeywords("amiga");
		SearchResponse resp = port.keywordSearch(keywordSearch);
		System.out.println(resp);
		return Status.OK;
	}

	protected RegistrySearchPortType getPort() throws MalformedURLException,
			ErrorResp {
		RegistryInterface service = new net.ivoa.wsdl.registryinterface.v1_0.RegistryInterface();
		RegistrySearchPortType registrySearchPort = service
				.getRegistrySearchPort();
		// Change binding hack as of
		// http://stackoverflow.com/questions/5158537/jaxws-how-to-change-the-endpoint-address
		// https://github.com/aviramsegal/snippets/blob/master/websphere/JaxWSCustomEndpoint.java
		BindingProvider bp = (BindingProvider) registrySearchPort;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				getEndpoint().toASCIIString());
		return registrySearchPort;

		// FIXME: Avoid evil ?wsdl hack

		/*
		 * RegistryInterface service = new RegistryInterface(getEndpoint()
		 * .resolve(getEndpoint().getPath() + "?wsdl").toURL());
		 * 
		 * 
		 * for (QName portName : new
		 * IterableIterator<QName>(service.getPorts())) { Object port =
		 * service.getPort(portName, RegistrySearchPortType.class); if (port
		 * instanceof RegistrySearchPortType) { return (RegistrySearchPortType)
		 * port; } } throw new
		 * IllegalStateException("Could not find any RegistrySearchPortType port"
		 * );
		 */

	}
}
