package org.purl.wf4ever.astrotaverna.pdl;


import java.util.List;
import java.util.Map;

import net.ivoa.pdl.interpreter.conditionalStatement.StatementHelperContainer;

/**
* @author Carlo Maria Zwolf
* Observatoire de Paris --
* LERMA
*/

public class ErrorSummary {
	private boolean hasJobError;
	private Map<String,List<StatementHelperContainer>> errorsPerGroup;

	public boolean getHasJobError() {
		return hasJobError;
	}
	
	public Map<String, List<StatementHelperContainer>> getErrorsPerGroup() {
		return errorsPerGroup;
	}
	
	public void setHasJobError(boolean hasJobError) {
		this.hasJobError = hasJobError;
	}
	
	public void setErrorsPerGroup(
		Map<String, List<StatementHelperContainer>> errorsPerGroup) {
		this.errorsPerGroup = errorsPerGroup;
	}

}