package uk.org.taverna.astro.astrotaverna.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import uk.org.taverna.astro.astrotaverna.AstroActivity;

public class AstroActivityContextViewFactory implements
		ContextualViewFactory<AstroActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof AstroActivity;
	}

	public List<ContextualView> getViews(AstroActivity selection) {
		return Arrays.<ContextualView>asList(new AstroContextualView(selection));
	}
	
}
