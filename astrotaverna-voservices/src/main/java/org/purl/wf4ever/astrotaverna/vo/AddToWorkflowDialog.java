package org.purl.wf4ever.astrotaverna.vo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import net.ivoa.xml.voresource.v1.Service;
import net.sf.taverna.t2.workbench.ui.impl.Workbench;

public class AddToWorkflowDialog extends JDialog {
	public class SetInputPort extends AbstractAction implements Action {
		private static final long serialVersionUID = 1L;
		private final JTextField textField;

		public SetInputPort(JTextField textField) {
			super("Input port");
			this.textField = textField;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBox box = (JCheckBox) e.getSource();
			this.textField.setEnabled(!box.isSelected());
		}
	}

	public class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddAction() {
			super("Add to workflow");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Map<String, String> parameters = getParameters();
			restServiceDescription.setParameters(parameters);
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

	protected Map<String, JTextField> fields = new HashMap<String, JTextField>();

	private boolean initialized = false;

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

	}

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		if (!initialized) {
			// Delayed initialization
			initialize();
			initialized = true;
		}
		super.show();
	}

	public Map<String, String> getParameters() {
		HashMap<String, String> parameters = new HashMap<String, String>();
		for (Entry<String, JTextField> s : fields.entrySet()) {
			String param = s.getKey();
			JTextField field = s.getValue();
			if (!field.getText().isEmpty() || !field.isEnabled()) {
				parameters.put(param, field.getText());
			}
		}
		return parameters;
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

		GridBagConstraints gbcOuter = new GridBagConstraints();
		gbcOuter.gridx = 0;
		gbcOuter.weightx = 0.2;
		gbcOuter.weighty = 0.0;
		gbcOuter.fill = GridBagConstraints.HORIZONTAL;
		gbcOuter.anchor = GridBagConstraints.CENTER;

		String message = String
				.format("<html><body><h3>Add %s to workflow</h3>"
						+ "<p>You may specify constant parameters here, or tick the <em>Input port</em> box to provide them in the workflow.</p>"
						+ "<p><p>Note: <b>Required</b> parameters will always appear as input ports "
						+ "in the workflow if no value is provided. The service might not support all optional parameters."
						+ "</body></html>", service.getShortName());
		add(new JLabel(message), gbcOuter);
		add(new JPanel(), gbcOuter);

		JPanel paramPanel = new JPanel(new GridBagLayout());
		gbcOuter.fill = GridBagConstraints.BOTH;
		gbcOuter.weighty = 1.0;
		add(new JScrollPane(paramPanel), gbcOuter);
		gbcOuter.weighty = 0.0;
		gbcOuter.fill = GridBagConstraints.HORIZONTAL;

		GridBagConstraints gbcLeft = new GridBagConstraints();
		gbcLeft.gridx = 0;
		gbcLeft.weightx = 0.2;
		gbcLeft.anchor = GridBagConstraints.LINE_END;

		GridBagConstraints gbcRight = new GridBagConstraints();
		gbcRight.gridx = 1;
		gbcRight.weightx = 0.7;
		gbcRight.fill = GridBagConstraints.HORIZONTAL;
		gbcRight.anchor = GridBagConstraints.LINE_START;

		GridBagConstraints gbcSuperRight = new GridBagConstraints();
		gbcSuperRight.gridx = 2;
		gbcSuperRight.weightx = 0.2;
		gbcSuperRight.anchor = GridBagConstraints.LINE_START;

		for (Entry<String, Boolean> entry : getModel().parametersForSearchType(
				null).entrySet()) {
			String param = entry.getKey();

			JLabel label = new JLabel();
			if ((entry.getValue())) {
				// required in bold
				label.setText(String.format(
						"<html><body><b>%s</b></body></html>", param));
			} else {
				label.setText(String.format("<html><body>%s</body></html>",
						param));
			}
			paramPanel.add(label, gbcLeft);
			JTextField textField = new JTextField(10);
			paramPanel.add(textField, gbcRight);
			label.setLabelFor(textField);
			fields.put(param, textField);

			JCheckBox inputPort = new JCheckBox();
			if (entry.getValue()) {
				inputPort.setSelected(true);
				inputPort.setEnabled(false);
			} else {
				inputPort.setAction(new SetInputPort(textField));
			}

			paramPanel.add(inputPort, gbcSuperRight);
		}

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(new JButton(addAction));
		buttonPanel.add(new JButton(cancelAction));
		add(buttonPanel, gbcOuter);

		// GridBagConstraints gbcFiller = new GridBagConstraints();
		// gbcFiller.gridx = 2;
		// gbcFiller.weightx = 1.0;
		// gbcFiller.fill = GridBagConstraints.HORIZONTAL;
		// add(new JPanel(), gbcFiller);

		setMinimumSize(new Dimension(450, 500));
	}

	public void setController(VOServicesController controller) {
		this.controller = controller;
	}

	public void setModel(VOServicesModel model) {
		this.model = model;
	}

}
