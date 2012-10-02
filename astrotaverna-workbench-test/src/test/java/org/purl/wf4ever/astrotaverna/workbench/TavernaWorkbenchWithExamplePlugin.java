package org.purl.wf4ever.astrotaverna.workbench;

import net.sf.taverna.t2.workbench.dev.DeveloperWorkbench;

/**
 * Run with parameters:
 * 
 * -Xmx300m -XX:MaxPermSize=140m 
 * 
 * NOTE: Do not save any workflows made using this test mode, as the plugin
 * information will be missing from the workflow file, and it will not open in a
 * Taverna run normally.
 * 
 */
public class TavernaWorkbenchWithExamplePlugin {
	public static void main(String[] args) throws Exception {
		System.setProperty("raven.launcher.app.name","taverna-2.4.0-dev");
		System.setProperty("taverna.startup",".");
		System.setProperty("sun.swing.enableImprovedDragGesture","");
		DeveloperWorkbench.main(args);
	}
}
