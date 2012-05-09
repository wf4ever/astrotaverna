package org.purl.wf4ever.astrotaverna.coordinates.ui.view;

import java.util.Arrays;
import java.util.List;

import org.purl.wf4ever.astrotaverna.coordinates.CoordinatesActivity;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;


public class CoordinatesActivityContextViewFactory implements
		ContextualViewFactory<CoordinatesActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof CoordinatesActivity;
	}

	public List<ContextualView> getViews(CoordinatesActivity selection) {
		return Arrays.<ContextualView>asList(new CoordinatesContextualView(selection));
	}
	
}
