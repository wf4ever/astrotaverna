package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectColumnsActivity;

public class StiltsActivityContextViewFactory implements
		ContextualViewFactory  {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		//return selection instanceof TjoinActivity;
		if(selection instanceof TjoinActivity)
			return true;
		else if(selection instanceof SelectColumnsActivity)
			return true;
		else
			return false;
		
	}

	//INCLUDE MORE METHODS LIKE THIS IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
	public List<ContextualView> getViews(TjoinActivity selection) {
		return Arrays.<ContextualView>asList(new StiltsContextualView(selection));
	}
	
	public List<ContextualView> getViews(SelectColumnsActivity selection) {
		return Arrays.<ContextualView>asList(new SelectColumnsContextualView(selection));
	}

	@Override
	public List<ContextualView> getViews(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof TjoinActivity)
			return getViews((TjoinActivity) arg0);
		else if(arg0 instanceof SelectColumnsActivity)
			return getViews((SelectColumnsActivity) arg0);
		else
			return null;
	}
	
}
