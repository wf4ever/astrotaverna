package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;

public class StiltsActivityContextViewFactory implements
		ContextualViewFactory<TjoinActivity> {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		return selection instanceof TjoinActivity;
		
	}

	//INCLUDE MORE METHODS LIKE THIS IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
	public List<ContextualView> getViews(TjoinActivity selection) {
		return Arrays.<ContextualView>asList(new StiltsContextualView(selection));
	}
	
}
