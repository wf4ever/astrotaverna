package uk.org.taverna.astro.vorepo;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Test;

public class TestVORepository {

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
	public void statusTimeout() throws Exception {
		assertEquals(VORepository.Status.CONNECTION_ERROR,
		// Probably nothing here
				new VORepository(URI.create("http://localhost:43651"))
						.getStatus());
	}

}
