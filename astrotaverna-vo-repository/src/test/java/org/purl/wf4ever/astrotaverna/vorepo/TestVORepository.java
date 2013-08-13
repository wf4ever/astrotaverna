package org.purl.wf4ever.astrotaverna.vorepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.BindingProvider;

import net.ivoa.xml.adql.v1.ClosedSearchType;
import net.ivoa.xml.adql.v1.IntersectionSearchType;
import net.ivoa.xml.adql.v1.LikePredType;
import net.ivoa.xml.adql.v1.SearchType;
import net.ivoa.xml.adql.v1.UnionSearchType;
import net.ivoa.xml.adql.v1.WhereType;
import net.ivoa.xml.adql.v1.XMatchType;
import net.ivoa.xml.conesearch.v1.ConeSearch;
import net.ivoa.xml.sia.v1.SimpleImageAccess;
import net.ivoa.xml.ssa.v0.SimpleSpectralAccess;
import net.ivoa.xml.voresource.v1.Capability;
import net.ivoa.xml.voresource.v1.Resource;
import net.ivoa.xml.voresource.v1.Service;

import org.junit.Ignore;
import org.junit.Test;
import org.purl.wf4ever.astrotaverna.vorepo.VORepository;
import org.purl.wf4ever.astrotaverna.wsdl.registrysearch.RegistrySearchPortType;


/*
 To debug SOAP messages, set system properties:

 -Dcom.sun.xml.ws.transport.http.client.HttpTransportPipe.dump=true
 -Dcom.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump=true
 */
public class TestVORepository {

	@Test
	public void coneSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo
				.resourceSearch(ConeSearch.class, "amiga");
		assertFalse(resources.isEmpty());
		Service s = resources.get(0);
		boolean foundCapability = false;
		for (Capability c : s.getCapability()) {
			if (c instanceof ConeSearch) {
				// ConeSearch coneSearch = (ConeSearch) c;
				foundCapability = true;
			}
		}
		assertTrue("Could not find any ConeSearch", foundCapability);
	}
	
	@Ignore("Takes a very long time")	
	@Test
	public void searchEveryService() throws Exception {
		BigInteger HUNDRED = BigInteger.valueOf(100);
		BigInteger ONE = BigInteger.valueOf(1);
		VORepository repo = new VORepository();
		WhereType where = new WhereType();
		where.setCondition(repo.makeLikeCondition("capability/interface/@xsi:type", "%"));
			// Perform search
		List<Resource> resources = null;
		BigInteger count = BigInteger.valueOf(0);
		while (resources == null || ! resources.isEmpty()) {
			resources = repo.getPort().search(where, count, count.add(HUNDRED), false)
				.getResource();
			for (Resource res : resources) {
				System.out.println(res);
				count = count.add(ONE);
			}
			Thread.sleep(500);
		}
		System.out.println(count);
		
	}


	@Test
	public void sdssDeserialization() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo
				.resourceSearch(ConeSearch.class, "SDSS", "DR8");
		assertFalse(resources.isEmpty());
		Service s = resources.get(0);
		boolean foundCapability = false;
		for (Capability c : s.getCapability()) {
			if (c instanceof ConeSearch) {
				// ConeSearch coneSearch = (ConeSearch) c;
				foundCapability = true;
			}
		}
		assertTrue("Could not find any ConeSearch", foundCapability);
	}
	
	@Test
	public void defaultConeSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo.resourceSearch(ConeSearch.class);
		assertTrue(resources.size() > 20);
	}

	@Test
	public void defaultRepo() throws Exception {
		VORepository repo = new VORepository();
		//assertEquals("http://registry.euro-vo.org/services/RegistrySearch",
		assertEquals("http://nvo.stsci.edu/vor10/ristandardservice.asmx",
				repo.getEndpoint().toASCIIString());
	}

	@Test
	public void defaultSIASearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo.resourceSearch(SimpleImageAccess.class);
		assertTrue(resources.size() > 20);
	}

	//jgs: I have commented this test because it was failing
	//@Ignore
	@Test
	public void defaultSPASearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo
				.resourceSearch(SimpleSpectralAccess.class);
		assertTrue(resources.size() > 20);
	}

	@Test
	public void emptyConeSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo.resourceSearch(ConeSearch.class,
				"ThisCertainlyShouldNotMatchRight192891");
		assertTrue(resources.isEmpty());
	}

	@Test
	public void endPointChanged() {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType firstPort = voRepository.getPort();
		assertEquals(
				//"http://registry.euro-vo.org/services/RegistrySearch",
				"http://nvo.stsci.edu/vor10/ristandardservice.asmx",
				((BindingProvider) firstPort).getRequestContext().get(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		voRepository.setEndpoint(URI.create("http://example.com/404"));
		RegistrySearchPortType secondPort = voRepository.getPort();
		assertNotSame(firstPort, secondPort);
		assertEquals(
				"http://example.com/404",
				((BindingProvider) secondPort).getRequestContext().get(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY));

	}

	@Test
	public void keywordSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Resource> resources = repo
				.keywordSearch("ivo://svo.amiga.iaa.es/amiga");
		assertFalse(resources.isEmpty());
		Resource someResource = resources.get(0);
		assertTrue(someResource.getIdentifier().startsWith("ivo://"));

	}

	@Test
	public void multipleConeSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo.resourceSearch(ConeSearch.class,
				"amiga", "J/A+A/462/507");
		assertEquals(1, resources.size());
	}

	@Test
	public void multipleEmptyConeSearch() throws Exception {
		VORepository repo = new VORepository();
		List<Service> resources = repo.resourceSearch(ConeSearch.class,
				"J/A+A/462/507", "ThisCertainlyShouldNotMatchRight192891");
		assertTrue(resources.isEmpty());
	}

	@Test
	public void portCached() throws Exception {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType port = voRepository.getPort();
		assertSame(port, voRepository.getPort());
	}

	@Test
	public void portUncached() throws Exception {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType port = voRepository.getPort();
		voRepository.setEndpoint(URI.create("http://example.com/404"));
		assertNotSame(port, voRepository.getPort());
	}

	@Test
	public void status() throws Exception {
		assertEquals(VORepository.Status.OK, new VORepository().getStatus());
	}

	@Test
	public void status404() throws Exception {
		assertEquals(VORepository.Status.CONNECTION_ERROR,
				new VORepository(URI.create("http://example.com/404"))
						.getStatus());
	}

	// Ignored as this adds 21s
	@Ignore
	@Test
	public void statusTimeout() throws Exception {
		assertEquals(VORepository.Status.CONNECTION_ERROR,
				new VORepository(URI.create("http://example.com:12345/"))
						.getStatus());
	}

	@Ignore  //jgs: this doesn't end with nvo as default registry (at least)
	@Test
	public void statusWrongEndpoint() throws Exception {
		assertEquals(
				VORepository.Status.CONNECTION_ERROR,
				new VORepository(
						URI.create("http://registry.euro-vo.org/services/RegistryHarvest"))
						.getStatus());
	}

}
