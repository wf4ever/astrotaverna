package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivityConfigurationBean;
import org.purl.wf4ever.astrotaverna.tjoin.ui.config.TemplateFillerConfigureAction;
//import org.purl.wf4ever.astrotaverna.tjoin.ui.config.StiltsConfigureAction;


@SuppressWarnings("serial")
public class TemplateFillerContextualView extends ContextualView {
	private final TemplateFillerActivity activity;
	private JTextArea description;
	private javax.swing.JScrollPane jScrollPane1;

	public TemplateFillerContextualView(TemplateFillerActivity activity) {
		this.activity = activity;
		initView(); //this method will call the getMainFrame()
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		description = new JTextArea();
		
		jPanel.setLayout(new java.awt.BorderLayout());
		
		description.setEditable(false);
		description.setColumns(30);
		description.setLineWrap(true);
		description.setText("The service receives a votable and a template whose keywords are replaced by the values in the votable." +
				"The keywords matches the format $keyword$, where keyword" +
				"corresponds necessarily to a column name in the votable." +
				"A file will be created for each row in the votable and its names correspond to the value given in the column" +
				"'ColumnNameWithResultFileName'. " +
				"Using the configure service option you can choose between direct input, " +
				"a URL or a File. ");
		
		jScrollPane1.setViewportView(description);
		jPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		//StiltsActivityConfigurationBean configuration = activity
		//		.getConfiguration();
		//return "Stilts service " + configuration.getExampleString();
		return "Template filler";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		//StiltsActivityConfigurationBean configuration = activity
		//		.getConfiguration();		
		//description.setText("Stilts service " + configuration.getExampleUri()
		//		+ " - " + configuration.getExampleString());
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
		
		
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	} 
	
	//the view can return a configuration Action if the selection can be configured 
	//or customized. If this is not null, Taverna will add a Configure button to 
	//the section.
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new TemplateFillerConfigureAction(activity, owner);
	}

}
