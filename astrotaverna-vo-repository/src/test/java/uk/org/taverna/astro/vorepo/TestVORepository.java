package uk.org.taverna.astro.vorepo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVORepository {
	
	@Test
	public void defaultRepo() throws Exception {
		VORepository repo = new VORepository();
		assertEquals("http://registry.euro-vo.org/services/RegistrySearch", repo.getEndpoint().toASCIIString());
	}
	
	@Test
	public void status() throws Exception {
		assertEquals(VORepository.Status.OK, new VORepository().getStatus());
	}
}
