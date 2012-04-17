package uk.org.taverna.astro.vorepo;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import net.ivoa.wsdl.registrysearch.v1.GetResource;
import net.ivoa.wsdl.registrysearch.v1.KeywordSearch;
import net.ivoa.wsdl.registrysearch.v1.SearchResponse;
import net.ivoa.xml.registryinterface.v1.VOResources;
import uk.org.taverna.astro.wsdl.registrysearch.ErrorResp;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchPortType;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchService;


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
		VOResources resp = port.keywordSearch("amiga", true, BigInteger.valueOf(0), BigInteger.valueOf(100), true);
		System.out.println(resp);
		System.out.println(resp.getResource());
		
		return Status.OK;
	}

	protected RegistrySearchPortType getPort() throws MalformedURLException,
			ErrorResp {		
		URL wsdlUri = getClass().getResource("/wsdl/dummySearch.wsdl");
		RegistrySearchService service = new RegistrySearchService(wsdlUri);
		RegistrySearchPortType port = service.getRegistrySearchPortSOAP();		
		// Change binding hack as of
		// http://stackoverflow.com/questions/5158537/jaxws-how-to-change-the-endpoint-address
		// https://github.com/aviramsegal/snippets/blob/master/websphere/JaxWSCustomEndpoint.java
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				getEndpoint().toASCIIString());
		return port;
	}
}
