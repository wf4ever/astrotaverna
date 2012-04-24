package uk.org.taverna.astro.vo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;

public class AddToWorkflowDialog extends JDialog {
	public class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddAction() {
			super("Add to workflow");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO: Pass parameters
			getController().addToWorkflow(restServiceDescription);
			dispose();
		}

	}

	public class CancelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public CancelAction() {
			super("Cancel");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	private static final long serialVersionUID = 1L;

	private VOServicesController controller;

	private VOServicesModel model;

	private final VOServiceDescription restServiceDescription;

	private final Service service;

	protected AddAction addAction = new AddAction();
	protected CancelAction cancelAction = new CancelAction();

	public AddToWorkflowDialog(VOServiceDescription restServiceDescription,
			Service service) {
		super(Workbench.getInstance());
		this.restServiceDescription = restServiceDescription;
		this.service = service;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getRootPane().registerKeyboardAction(new ActionListener() {
			// http://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		initialize();
	}

	public VOServicesController getController() {
		return controller;
	}

	public VOServicesModel getModel() {
		return model;
	}

	public VOServiceDescription getRestServiceDescription() {
		return restServiceDescription;
	}

	public Service getService() {
		return service;
	}

	protected void initialize() {
		setTitle("Add VO service to workflow");
		setLayout(new GridBagLayout());
	
		GridBagConstraints gbcLeft = new GridBagConstraints();
		gbcLeft.gridx = 0;
		gbcLeft.weightx = 0.2;
		gbcLeft.anchor = GridBagConstraints.LINE_END;

		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcRight.gridx = 1;
		gbcRight.weightx = 0.7;
		gbcRight.fill = GridBagConstraints.HORIZONTAL;
		gbcRight.anchor = GridBagConstraints.LINE_START;

		GridBagConstraints gbcBoth = new GridBagConstraints();
		gbcBoth.gridx = 0;
		gbcBoth.gridwidth = 2;
		gbcBoth.weightx = 0.2;
		gbcBoth.weighty = 0.0;
		gbcBoth.fill = GridBagConstraints.HORIZONTAL;
		gbcBoth.anchor = GridBagConstraints.CENTER;

		add(new JLabel(String.format("<html><body>"
				+ "<h3>Add %s to workflow</h3>"
				+ "<p>You may specify constant parameters here, or leave the "
				+ "parameter field blank to supply the field by connecting "
				+ "the corresponding input port the workflow.</p>"
				+ "</body></html>", service.getShortName())), gbcBoth);
		add(new JPanel(), gbcBoth);

		// TODO: Work out the real parameters
		add(new JLabel("POS"), gbcLeft);
		add(new JTextField(10), gbcRight);

		add(new JLabel("SIZE"), gbcLeft);
		add(new JTextField(10), gbcRight);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(new JButton(addAction));
		buttonPanel.add(new JButton(cancelAction));
		add(buttonPanel, gbcBoth);
	

		GridBagConstraints gbcFiller = new GridBagConstraints();
		gbcRight.gridx = 2;
		gbcRight.weightx = 1.0;
		gbcRight.fill = GridBagConstraints.HORIZONTAL;
		add(new JPanel(), gbcFiller);
		
		setMinimumSize(new Dimension(450, 300));
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

}
