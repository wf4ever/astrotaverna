package uk.org.taverna.astro.vorepo;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import net.ivoa.wsdl.registrysearch.v1.ResolveResponse;
import net.ivoa.xml.adql.v1.AtomType;
import net.ivoa.xml.adql.v1.ColumnReferenceType;
import net.ivoa.xml.adql.v1.IntersectionSearchType;
import net.ivoa.xml.adql.v1.LikePredType;
import net.ivoa.xml.adql.v1.StringType;
import net.ivoa.xml.adql.v1.WhereType;
import net.ivoa.xml.registryinterface.v1.VOResources;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Resource;
import net.ivoa.xml.voresource.v1.Service;
import uk.org.taverna.astro.wsdl.registrysearch.ErrorResp;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchPortType;
import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchService;

public class VORepository {

	public enum Status {
		OK, ERROR, CONNECTION_ERROR, UNKNOWN;
	}

	private final static QName REGISTRYSEARCHSERVICE_QNAME = new QName(
			"http://taverna.org.uk/astro/wsdl/RegistrySearch",
			"RegistrySearchService");

	public static final URI DEFAULT_ENDPOINT = URI
			.create("http://registry.euro-vo.org/services/RegistrySearch");

	public static final URI WSDL = URI
			.create("http://www.ivoa.net/wsdl/RegistrySearch/v1.0");

	private URI endpoint;

	private RegistrySearchPortType port;

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
		synchronized (this) {
			this.endpoint = endpoint;
			// Force getPort() to re-evaluate
			this.port = null;
		}
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
		synchronized (this) {
			if (this.port != null) {
				return this.port;
			}
		}
		URL wsdlUri = getClass().getResource("/wsdl/dummySearch.wsdl");

		RegistrySearchService service = new RegistrySearchService(wsdlUri,
				REGISTRYSEARCHSERVICE_QNAME);
		RegistrySearchPortType port = service.getRegistrySearchPortSOAP();
		// Change binding hack as of
		// http://stackoverflow.com/questions/5158537/jaxws-how-to-change-the-endpoint-address
		// https://github.com/aviramsegal/snippets/blob/master/websphere/JaxWSCustomEndpoint.java
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				getEndpoint().toASCIIString());

		synchronized (this) {
			this.port = port;
		}
		return port;
	}

	public List<Resource> keywordSearch(String keywords) throws ErrorResp {
		VOResources voResources = getPort().keywordSearch(keywords, true, null,
				null, null);		
		return voResources.getResource();
	}

	@SuppressWarnings("unchecked")
	public List<Service> resourceSearch(Class<? extends Capability> capabilityType, String... keywords) throws ErrorResp {		
		WhereType where = new WhereType();
		
		IntersectionSearchType and = new IntersectionSearchType();
		where.setCondition(and);

		
		XmlType xmlType = capabilityType.getAnnotation(javax.xml.bind.annotation.XmlType.class);
		String xsiTypeValueLiteral = "%" + xmlType.name();
		//  TODO: namespaced xsi:type lookup without using % trick?
		
		and.getCondition().add(makeLikeCondition("capability/@xsi:type", xsiTypeValueLiteral));
		for (String kw : keywords) {
			and.getCondition().add(makeLikeCondition("content/description", "%" + kw + "%"));
		}
		if (and.getCondition().size() == 1) {
			// Drop the or
			where.setCondition(and.getCondition().get(0));
		}
		
		
		// Perform search
		List<Resource> resources = getPort().search(where, null, null, false).getResource();

		// Just double-checking the capabilities as xsi:type check above is fragile
		// Bonus: Convert to List<Service>
		List<Service> services =  new ArrayList<Service>();
		for (Resource res : resources) {
			if (! (res instanceof Resource)) {
				continue;
			}
			Service ser = (Service) res;
			for (Capability c : ser.getCapability()) {
				if (capabilityType.isInstance(c)) {
					services.add(ser);
					break;
				}
			}
		}
		return services;
	}

	private LikePredType makeLikeCondition(String xpath,
			String literal) {
		LikePredType like = new LikePredType();
		ColumnReferenceType typeArg = new ColumnReferenceType();
		typeArg.setName("arg-" + UUID.randomUUID().toString());
		typeArg.setTable("");		
		typeArg.setXpathName(xpath);
		like.setArg(typeArg);
		AtomType pattern = new AtomType();
		StringType value = new StringType();
		value.setValue(literal);		
		pattern.setLiteral(value);
		like.setPattern(pattern);		
		return like;
	}

}
