package org.purl.wf4ever.astrotaverna.tjoin.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import org.purl.wf4ever.astrotaverna.tcat.TcatActivity;
import org.purl.wf4ever.astrotaverna.tcat.TcatListActivity;
import org.purl.wf4ever.astrotaverna.tjoin.TjoinActivity;
import org.purl.wf4ever.astrotaverna.tpipe.AddColumnByExpressionActivity;
import org.purl.wf4ever.astrotaverna.tpipe.AddSkyCoordsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.CoordTransformationActivity;
import org.purl.wf4ever.astrotaverna.tpipe.FormatConversionActivity;
import org.purl.wf4ever.astrotaverna.tpipe.ResolveCoordsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectColumnsActivity;
import org.purl.wf4ever.astrotaverna.tpipe.SelectRowsActivity;
import org.purl.wf4ever.astrotaverna.voutils.CheckTemplateFillerActivity;
import org.purl.wf4ever.astrotaverna.voutils.GetListFromColumnActivity;
import org.purl.wf4ever.astrotaverna.voutils.TemplateFillerActivity;

public class StiltsActivityContextViewFactory implements
		ContextualViewFactory  {

	public boolean canHandle(Object selection) {
		//CHANGE IS THERE IS MORE THAN ONE TYPE OF ACTIVITY
		//return selection instanceof TjoinActivity;
		if(selection instanceof TjoinActivity)
			return true;
		else if(selection instanceof SelectColumnsActivity)
			return true;
		else if(selection instanceof SelectRowsActivity)
			return true;
		else if(selection instanceof CoordTransformationActivity)
			return true;
		else if(selection instanceof FormatConversionActivity)
			return true;
		else if(selection instanceof AddColumnByExpressionActivity)
			return true;
		else if(selection instanceof AddSkyCoordsActivity)
			return true;
		else if(selection instanceof ResolveCoordsActivity)
			return true;
		else if(selection instanceof TcatActivity)
			return true;
		else if(selection instanceof TcatListActivity)
			return true;
		else if(selection instanceof GetListFromColumnActivity)
			return true;
		else if(selection instanceof TemplateFillerActivity)
			return true;
		else if(selection instanceof CheckTemplateFillerActivity)
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
	
	public List<ContextualView> getViews(SelectRowsActivity selection) {
		return Arrays.<ContextualView>asList(new SelectRowsContextualView(selection));
	}
	
	public List<ContextualView> getViews(CoordTransformationActivity selection) {
		return Arrays.<ContextualView>asList(new CoordTransformationContextualView(selection));
	}
	
	public List<ContextualView> getViews(FormatConversionActivity selection) {
		return Arrays.<ContextualView>asList(new FormatConversionContextualView(selection));
	}
	
	public List<ContextualView> getViews(AddColumnByExpressionActivity selection) {
		return Arrays.<ContextualView>asList(new AddColumnByExpressionContextualView(selection));
	}
	
	public List<ContextualView> getViews(AddSkyCoordsActivity selection) {
		return Arrays.<ContextualView>asList(new AddSkyCoordsContextualView(selection));
	}
	
	public List<ContextualView> getViews(ResolveCoordsActivity selection) {
		return Arrays.<ContextualView>asList(new ResolveCoordsContextualView(selection));
	}
	
	public List<ContextualView> getViews(TcatActivity selection) {
		return Arrays.<ContextualView>asList(new TcatContextualView(selection));
	}
	
	public List<ContextualView> getViews(TcatListActivity selection) {
		return Arrays.<ContextualView>asList(new TcatListContextualView(selection));
	}
	
	public List<ContextualView> getViews(GetListFromColumnActivity selection) {
		return Arrays.<ContextualView>asList(new GetListFromColumnContextualView(selection));
	}											 
	
	public List<ContextualView> getViews(TemplateFillerActivity selection) {
		return Arrays.<ContextualView>asList(new TemplateFillerContextualView(selection));
	}
	
	public List<ContextualView> getViews(CheckTemplateFillerActivity selection) {
		return Arrays.<ContextualView>asList(new CheckTemplateFillerContextualView(selection));
	}
	
	@Override
	public List<ContextualView> getViews(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof TjoinActivity)
			return getViews((TjoinActivity) arg0);
		else if(arg0 instanceof SelectColumnsActivity)
			return getViews((SelectColumnsActivity) arg0);
		else if(arg0 instanceof SelectRowsActivity)
			return getViews((SelectRowsActivity) arg0);
		else if(arg0 instanceof CoordTransformationActivity)
			return getViews((CoordTransformationActivity) arg0);
		else if(arg0 instanceof FormatConversionActivity)
			return getViews((FormatConversionActivity) arg0);
		else if(arg0 instanceof AddColumnByExpressionActivity)
			return getViews((AddColumnByExpressionActivity) arg0);
		else if(arg0 instanceof AddSkyCoordsActivity)
			return getViews((AddSkyCoordsActivity) arg0);
		else if(arg0 instanceof ResolveCoordsActivity)
			return getViews((ResolveCoordsActivity) arg0);
		else if(arg0 instanceof TcatActivity)
			return getViews((TcatActivity) arg0);
		else if(arg0 instanceof TcatListActivity)
			return getViews((TcatListActivity) arg0);
		else if(arg0 instanceof GetListFromColumnActivity)
			return getViews((GetListFromColumnActivity) arg0);
		else if(arg0 instanceof TemplateFillerActivity)
			return getViews((TemplateFillerActivity) arg0);
		else if(arg0 instanceof CheckTemplateFillerActivity)
			return getViews((CheckTemplateFillerActivity) arg0);
		else
			return null;
	}
	
}
