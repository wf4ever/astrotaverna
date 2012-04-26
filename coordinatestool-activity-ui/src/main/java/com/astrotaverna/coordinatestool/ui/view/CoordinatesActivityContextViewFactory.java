package com.astrotaverna.coordinatestool.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import com.astrotaverna.coordinatestool.CoordinatesActivity;

public class CoordinatesActivityContextViewFactory implements
		ContextualViewFactory<CoordinatesActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof CoordinatesActivity;
	}

	public List<ContextualView> getViews(CoordinatesActivity selection) {
		return Arrays.<ContextualView>asList(new CoordinatesContextualView(selection));
	}
	
}
