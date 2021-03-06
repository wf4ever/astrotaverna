package org.purl.wf4ever.astrotaverna.vo.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	private static final String EMPTY = "<html><body></body></html>";
	private static Logger logger = Logger.getLogger(HTMLPane.class);

	private static final long serialVersionUID = 5655697117597352598L;

	public HTMLPane() {
		super("text/html", EMPTY);
		initialize();
	}

	public HTMLPane(String text) {
		super("text/html", text);
		initialize();
	}

	public HTMLPane(URL url) throws IOException {
		super(url);
		initialize();
	}

	protected void initialize() {
		setEditable(false);
		addHyperlinkListener(new OpenInBrowserHyperlinkListener());
	}

	public void reset() {
		setText(EMPTY);
	}

	@Override
	public void setText(String t) {
		super.setText(t);
		setCaretPosition(0);
	}

	/**
	 * HTML-safe version of String.format
	 * <p>
	 * Characters &amp;, &lt; and &gt; will be substituted with the HTML
	 * entities. <code>null</code> is replaced with an empty string. 
	 * If all arguments are <code>null</code>, an empty string is returned
	 * instead of the formatted string, unless there was no arguments.
	 * 
	 * @see String#format(String, Object...)
	 * @param format
	 *            The format
	 * @param args
	 *            Arguments for the format
	 * @return Formatted string
	 */
	public static String format(String format, Object... args) {		
		List<String> strings = new ArrayList<String>();
		boolean foundValue = false;
		for (Object arg : args) {
			if (arg == null) {
				strings.add("");
				continue;
			}
			foundValue = true;
			strings.add(arg.toString().replace("&", "&amp;")
					.replace("<", "&lt;").replace(">", "&gt;"));
		}
		if (! foundValue && args.length > 1) {
			return "";
		}
		return String.format(format, strings.toArray());
	}
}