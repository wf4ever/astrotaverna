package org.purl.wf4ever.astrotaverna.pdl.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;

import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Processor;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;
import net.sf.taverna.t2.workflowmodel.utils.Tools;


public class PDLActivityContextViewFactory implements
		ContextualViewFactory  {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		//return selection instanceof TjoinActivity;

		//net.sf.taverna.t2.workflowmodel.processor.activity.impl.ActivityInputPort
		if(selection instanceof ActivityInputPort){
			try{
				Dataflow dataflow = FileManager.getInstance().getCurrentDataflow();
				Processor processor = Tools.getFirstProcessorWithActivityInputPort(dataflow, (ActivityInputPort) selection);
				List list =  processor.getActivityList();
				Activity activity = (Activity) list.get(0);
				if(activity instanceof PDLServiceActivity)
					return true;
				else
					return false;
			}catch (NullPointerException ex){
				ex.printStackTrace();
				return false;
			}
		}else if(selection instanceof ActivityOutputPort){
			try{
				Dataflow dataflow = FileManager.getInstance().getCurrentDataflow();
				Processor processor = Tools.getFirstProcessorWithActivityOutputPort(dataflow, (ActivityOutputPort) selection);
				List list =  processor.getActivityList();
				Activity activity = (Activity) list.get(0);
				if(activity instanceof PDLServiceActivity)
					return true;
				else
					return false;
			}catch (NullPointerException ex){
				ex.printStackTrace();
				return false;
			}
		}else if(selection instanceof ValidationPDLClientActivity)
			return true;
		else if(selection instanceof PDLServiceActivity)
			return true;
		else
			return false;
		
	}

	//INCLUDE MORE METHODS LIKE THIS IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		
	public List<ContextualView> getViews(ActivityInputPort selection) {
		Dataflow dataflow = FileManager.getInstance().getCurrentDataflow();
		Processor processor = Tools.getFirstProcessorWithActivityInputPort(dataflow, (ActivityInputPort) selection);
		List list =  processor.getActivityList();
		Activity activity = (Activity) list.get(0);
		if(activity instanceof PDLServiceActivity)
			return Arrays.<ContextualView>asList(new PDLPortContextualView(selection, (PDLServiceActivity) activity));
		else
			return Arrays.<ContextualView>asList();
	}
	
	public List<ContextualView> getViews(ActivityOutputPort selection) {
		Dataflow dataflow = FileManager.getInstance().getCurrentDataflow();
		Processor processor = Tools.getFirstProcessorWithActivityOutputPort(dataflow, (ActivityOutputPort) selection);
		List list =  processor.getActivityList();
		Activity activity = (Activity) list.get(0);
		if(activity instanceof PDLServiceActivity)
			return Arrays.<ContextualView>asList(new PDLPortContextualView(selection, (PDLServiceActivity) activity));
		else
			return Arrays.<ContextualView>asList();
	}
	
	public List<ContextualView> getViews(ValidationPDLClientActivity selection) {
		return Arrays.<ContextualView>asList(new ValidationPDLClientContextualView(selection));
	}
	
	public List<ContextualView> getViews(PDLServiceActivity selection) {
		return Arrays.<ContextualView>asList(new PDLServiceContextualView(selection));
	}
	
	
	
	@Override
	public List<ContextualView> getViews(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof ActivityInputPort)
			return getViews((ActivityInputPort) arg0);
		else if(arg0 instanceof ActivityOutputPort)
			return getViews((ActivityOutputPort) arg0);
		else if(arg0 instanceof ValidationPDLClientActivity)
			return getViews((ValidationPDLClientActivity) arg0);
		else if(arg0 instanceof PDLServiceActivity)
			return getViews((PDLServiceActivity) arg0);
		else
			return null;
	}
	
}
