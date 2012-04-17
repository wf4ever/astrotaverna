package uk.org.taverna.astro.vorepo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.net.URI;

import javax.xml.ws.BindingProvider;

import org.junit.Ignore;
import org.junit.Test;

import uk.org.taverna.astro.wsdl.registrysearch.RegistrySearchPortType;

public class TestVORepository {

	@Test
	public void keywordSearch() throws Exception {
		VORepository repo = new VORepository();
		String resources = repo.keywordSearch("amiga");
	}

	@Test
	public void defaultRepo() throws Exception {
		VORepository repo = new VORepository();
		assertEquals("http://registry.euro-vo.org/services/RegistrySearch",
				repo.getEndpoint().toASCIIString());
	}

	@Test
	public void status() throws Exception {
		assertEquals(VORepository.Status.OK, new VORepository().getStatus());
	}

	@Test
	public void portCached() throws Exception {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType port = voRepository.getPort();
		assertSame(port, voRepository.getPort());
	}

	@Test
	public void statusWrongEndpoint() throws Exception {
		assertEquals(
				VORepository.Status.CONNECTION_ERROR,
				new VORepository(
						URI.create("http://registry.euro-vo.org/services/RegistryHarvest"))
						.getStatus());
	}

	@Test
	public void status404() throws Exception {
		assertEquals(VORepository.Status.CONNECTION_ERROR,
				new VORepository(URI.create("http://example.com/404"))
						.getStatus());
	}

	@Test
	public void endPointChanged() {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType port = voRepository.getPort();
		assertEquals(
				"http://registry.euro-vo.org/services/RegistrySearch",
				((BindingProvider) port).getRequestContext().get(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		voRepository.setEndpoint(URI.create("http://example.com/404"));
		assertNotSame(port, voRepository.getPort());
		assertEquals(
				"http://example.com/404",
				((BindingProvider) port).getRequestContext().get(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY));

	}

	@Test
	public void portUncached() throws Exception {
		VORepository voRepository = new VORepository();
		RegistrySearchPortType port = voRepository.getPort();
		voRepository.setEndpoint(URI.create("http://example.com/404"));
		assertNotSame(port, voRepository.getPort());
	}

	// Ignored as this adds 21s
	@Ignore
	@Test
	public void statusTimeout() throws Exception {
		assertEquals(VORepository.Status.CONNECTION_ERROR,
				new VORepository(URI.create("http://example.com:12345/"))
						.getStatus());
	}

}
