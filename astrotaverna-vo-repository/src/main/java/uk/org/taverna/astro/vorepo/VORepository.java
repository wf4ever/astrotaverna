package uk.org.taverna.astro.vorepo;

import java.net.URI;
import java.net.URL;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import net.ivoa.wsdl.registrysearch.v1.ResolveResponse;
import uk.org.taverna.astro.wsdl.registrysearch.ErrorResp;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchPortType;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchService;

public class VORepository {

	public enum Status {
		OK, ERROR, CONNECTION_ERROR, UNKNOWN;
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

	public Status getStatus() {
		RegistrySearchPortType port = getPort();
		ResolveResponse id;
		try {
			id = port.getIdentity("");
		} catch (WebServiceException e) {
			return Status.CONNECTION_ERROR;
		} catch (ErrorResp e) {
			return Status.ERROR;
		}
		try {
			if (id.getResource().getStatus().equalsIgnoreCase("active")) {
				return Status.OK;
			} else {
				return Status.UNKNOWN;
			}
		} catch (NullPointerException ex) {
			return Status.ERROR;
		}

	}

	protected RegistrySearchPortType getPort() {
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
