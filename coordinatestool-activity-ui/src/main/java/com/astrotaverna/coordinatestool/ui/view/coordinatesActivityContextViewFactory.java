package com.astrotaverna.coordinatestool.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import com.astrotaverna.coordinatestool.coordinatesActivity;

public class coordinatesActivityContextViewFactory implements
		ContextualViewFactory<coordinatesActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof coordinatesActivity;
	}

	public List<ContextualView> getViews(coordinatesActivity selection) {
		return Arrays.<ContextualView>asList(new coordinatesContextualView(selection));
	}
	
}
