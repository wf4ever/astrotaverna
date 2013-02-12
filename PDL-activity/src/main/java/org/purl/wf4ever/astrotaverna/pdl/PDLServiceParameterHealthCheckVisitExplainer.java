package org.purl.wf4ever.astrotaverna.pdl;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.taverna.t2.lang.ui.ReadOnlyTextArea;
import net.sf.taverna.t2.visit.VisitKind;
import net.sf.taverna.t2.visit.VisitReport;
import net.sf.taverna.t2.workbench.report.explainer.VisitExplainer;
import net.sf.taverna.t2.workbench.report.view.ReportViewConfigureAction;
import net.sf.taverna.t2.workflowmodel.Processor;

//important status constants
import static org.purl.wf4ever.astrotaverna.pdl.PDLServiceParameterHealthCheck.*;

/**
 * 
 * @author Julian Garrido
 */
public class PDLServiceParameterHealthCheckVisitExplainer implements VisitExplainer{

	@Override
	public boolean canExplain(VisitKind vk, int resultId) {
		return (vk instanceof PDLServiceParameterHealthCheck);
	}

	@Override
	public JComponent getExplanation(VisitReport vr) {
		// TODO Auto-generated method stub
		String explanation = "";
		explanation = "puedo explicarlo";
		return new ReadOnlyTextArea(explanation);
	}

	@Override
	public JComponent getSolution(VisitReport vr) {
		// TODO Auto-generated method stub
		int resultId = vr.getResultId();
	    String explanation = null;
	    boolean includeConfigButton = false;
	    
	    explanation = "puedo solucionarlo";
	    
	    JPanel jpSolution = new JPanel();
	    jpSolution.setLayout(new BoxLayout(jpSolution, BoxLayout.Y_AXIS));
	    
	    ReadOnlyTextArea taExplanation = new ReadOnlyTextArea(explanation);
	    taExplanation.setAlignmentX(Component.LEFT_ALIGNMENT);
	    jpSolution.add(taExplanation);
	    
	    if (includeConfigButton)
	    {
	      JButton button = new JButton();
	      Processor p = (Processor) (vr.getSubject());
	      button.setAction(new ReportViewConfigureAction(p));
	      button.setText("Open REST Activity configuration dialog");
	      button.setAlignmentX(Component.LEFT_ALIGNMENT);
	      
	      jpSolution.add(button);
	    }
	    
	    
	    return (jpSolution);
	    
	}
	
	

}
