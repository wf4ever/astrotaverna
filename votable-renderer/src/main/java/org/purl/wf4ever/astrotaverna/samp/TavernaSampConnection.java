package org.purl.wf4ever.astrotaverna.samp;

import org.astrogrid.samp.Metadata;
import org.astrogrid.samp.client.ClientProfile;
import org.astrogrid.samp.client.DefaultClientProfile;
import org.astrogrid.samp.gui.GuiHubConnector;
import org.purl.wf4ever.astrotaverna.view.votable.VOTableRenderer;

public class TavernaSampConnection {

	private static GuiHubConnector hubConnector;

	public static GuiHubConnector getSampHubConnector() {
		if (hubConnector != null) {
			return hubConnector;
		}
		synchronized(VOTableRenderer.class) {
			if (hubConnector != null) {
				return hubConnector;
			}
			ClientProfile profile = DefaultClientProfile.getProfile();
			GuiHubConnector conn = new GuiHubConnector(profile);
	
			// Register ourselves
			Metadata meta = new Metadata();
			meta.setName("Taverna");
			meta.setDescriptionText("Taverna workbench");
			conn.declareMetadata(meta);
	
			// This step required even if no custom message handlers added.
			conn.declareSubscriptions(conn.computeSubscriptions());
	
			// Keep a look out for hubs if initial one shuts down
			conn.setAutoconnect(10);
			hubConnector = conn;
			return hubConnector;
		}
	}
}
