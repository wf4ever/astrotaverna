package org.purl.wf4ever.astrotaverna.pdl.ui.menu;

import javax.swing.Action;


import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;
import org.purl.wf4ever.astrotaverna.pdl.PDLServiceActivity;
import org.purl.wf4ever.astrotaverna.pdl.ui.config.PDLServiceConfigureAction;

/**
 * This action is responsible for enabling the contextual menu entry
 * on processors that perform PDLActivity'ies.
 * 
 * NB! As a side-effect this also enables the pop-up with for configuration
 * of the processor when it is added to the workflow from the Service Panel. 
 * 
 * @author Julian Garrido
 */
public class PDLServiceConfigureMenuAction extends
	AbstractConfigureActivityMenuAction<PDLServiceActivity> 
{

	public PDLServiceConfigureMenuAction() {
		super(PDLServiceActivity.class);
	}

	@Override
	protected Action createAction() {
		PDLServiceConfigureAction configAction = new PDLServiceConfigureAction(
		        findActivity(), getParentFrame());
		    configAction.putValue(Action.NAME, "Configure service");
		    addMenuDots(configAction);
		    return configAction;
		/*
		PDLServiceActivity a = findActivity();
		Action result = null;
		result = new PDLServiceConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure service");
		addMenuDots(result);
		return result;
		*/
	}

}
