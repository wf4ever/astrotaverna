package org.purl.wf4ever.astrotaverna.pdl;

import net.sf.taverna.t2.visit.VisitKind;
import net.sf.taverna.t2.visit.Visitor;


/**
* A <code>PDLServiceParameterHealthCheck</code> is a kind of visit that determines
* if the corresponding PDLService activity in a workflow will work during a workflow run.
* 
* @author Julian Garrido
*/
public class PDLServiceParameterHealthCheck extends VisitKind {

	//binary masks for the status of the checker =>> you can do the binary combination. 
	public static final int NO_ERROR = 0;
	public static final int PRECISION_ERROR = 1;
	public static final int TYPE_ERROR = 2;
	public static final int UTYPE_ERROR = 4;
	public static final int UCD_ERROR = 8;
	public static final int UNIT_ERROR = 16;
	public static final int SKOS_ERROR = 32;
	public static final int UNKNOWN = 64;
	
	
	@Override
	public Class<? extends Visitor> getVisitorClass() {
		return PDLServiceParameterHealthChecker.class;
	}

	private static class Singleton {
	    private static PDLServiceParameterHealthCheck instance = new PDLServiceParameterHealthCheck();
	}
	 
	public static PDLServiceParameterHealthCheck getInstance() {
	    return Singleton.instance;
	}
	
}
