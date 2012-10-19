package org.purl.wf4ever.astrotaverna.pdl.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.purl.wf4ever.astrotaverna.pdl.ValidationPDLClientActivity;


public class PDLActivityContextViewFactory implements
		ContextualViewFactory  {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		//return selection instanceof TjoinActivity;
		if(selection instanceof ValidationPDLClientActivity)
			return true;
		else
			return false;
		
	}

	//INCLUDE MORE METHODS LIKE THIS IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		
	public List<ContextualView> getViews(ValidationPDLClientActivity selection) {
		return Arrays.<ContextualView>asList(new ValidationPDLClientContextualView(selection));
	}
	
	@Override
	public List<ContextualView> getViews(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof ValidationPDLClientActivity)
			return getViews((ValidationPDLClientActivity) arg0);
		else
			return null;
	}
	
}
