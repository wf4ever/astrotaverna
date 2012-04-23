package uk.org.taverna.astro.vo;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;

/**
 * A {@link JEditorPane} for showing (simple) HTML.
 * <p>
 * Includes a hyperlink listener that opens links in the desktop default
 * browser.
 * 
 * @author Stian Soiland-Reyes
 * 
 */
public class HTMLPane extends JEditorPane {
	private static final long serialVersionUID = 5655697117597352598L;
	private static Logger logger = Logger.getLogger(HTMLPane.class);

	public HTMLPane(URL url) throws IOException {
		super(url);
		initialize();
	}

	public HTMLPane(String text) {
		super("text/html", text);
		initialize();
	}


	protected void initialize() {
		setEditable(false);
		addHyperlinkListener(new OpenInBrowserHyperlinkListener());
	}
	
	public class OpenInBrowserHyperlinkListener implements HyperlinkListener {

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {
			HyperlinkEvent.EventType type = e.getEventType();
			if (type == HyperlinkEvent.EventType.ACTIVATED) {
				// Open a Web browser
				URL url = e.getURL();
				try {
					Desktop.getDesktop().browse(url.toURI());
				} catch (IOException e1) {
					logger.warn("Could not open browser for " + url, e1);
				} catch (URISyntaxException e1) {
					logger.warn("Invalid URI " + url, e1);
				}
			}
		}
	}

}