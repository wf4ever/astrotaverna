package org.purl.wf4ever.astrotaverna.image.ui.view;

import java.util.Arrays;
import java.util.List;

import org.purl.wf4ever.astrotaverna.aladin.AladinMacroActivity;
import org.purl.wf4ever.astrotaverna.aladin.AladinScriptActivity;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;


public class ImageActivityContextViewFactory implements
		ContextualViewFactory  {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY

		if(selection instanceof AladinScriptActivity)
			return true;
		else if(selection instanceof AladinMacroActivity)
			return true;
		else
			return false;
		
	}

	//INCLUDE MORE METHODS LIKE THIS IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
	public List<ContextualView> getViews(AladinScriptActivity selection) {
		return Arrays.<ContextualView>asList(new AladinScriptContextualView(selection));
	}
	
	public List<ContextualView> getViews(AladinMacroActivity selection) {
		return Arrays.<ContextualView>asList(new AladinMacroContextualView(selection));
	}

	
		
	@Override
	public List<ContextualView> getViews(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof AladinScriptActivity)
			return getViews((AladinScriptActivity) arg0);
		else if(arg0 instanceof AladinMacroActivity)
			return getViews((AladinMacroActivity) arg0);
		else 
			return null;
	}
	
}
