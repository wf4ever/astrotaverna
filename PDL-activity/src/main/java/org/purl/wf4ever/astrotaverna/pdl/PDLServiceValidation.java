package org.purl.wf4ever.astrotaverna.pdl;

import java.util.ArrayList;
import java.util.List;

import net.ivoa.pdl.interpreter.conditionalStatement.StatementHelperContainer;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupHandlerHelper;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupProcessor;
import net.ivoa.pdl.interpreter.utilities.Utilities;

public class PDLServiceValidation {

	private GroupProcessor groupProcessor;
	
	final public String complete = "To complete";
	final public String error = "With error";
	final public String valid = "Valid";
	
	public PDLServiceValidation(GroupProcessor gp){
		this.groupProcessor = gp;
	}
	
	public String validate() {
		groupProcessor.process();
		String status =""; 

		List<String> infosOnGroups = this.getInfosOnGroups();
		if ((infosOnGroups.get(0) == null || infosOnGroups.get(0).equalsIgnoreCase(""))
				&& (infosOnGroups.get(1) == null || infosOnGroups.get(1).equalsIgnoreCase(""))) {	
			status = valid;
		}

		if (!infosOnGroups.get(0).equalsIgnoreCase("")) {
			status = complete;
		}

		if (!infosOnGroups.get(1).equalsIgnoreCase("")) {
			status = error;
		}

		//if (!infosOnGroups.get(2).equalsIgnoreCase("")) {
		//	status = valid;
		//}
		
		return status;

	}
	
	public boolean isValid(){
		return this.validate().compareTo(valid)==0;
	}
	
	public String getStatus(){
		return this.validate();
	}
	
	void launchComputation(){
		if(this.isValid())
			Utilities.getInstance().callService();
	}
	
	
	private List<String> getInfosOnGroups() {
		List<String> toReturn = new ArrayList<String>();

		toReturn.add(this.buildStringFromList(this.getGroupsToComplete()));
		toReturn.add(this.buildStringFromList(this.getGroupsWithError()));
		toReturn.add(this.buildStringFromList(this.getGroupsValid()));

		return toReturn;
	}

	private String buildStringFromList(List<String> list) {
		String toReturn = "";
		for (String temp : list) {
			toReturn = toReturn + temp + "\n";
		}
		return toReturn;
	}

	private List<String> getGroupsToComplete() {
		List<GroupHandlerHelper> handler = this.groupProcessor
				.getGroupsHandler();
		List<String> toReturn = new ArrayList<String>();

		// Loop for every group
		for (int i = 0; i < handler.size(); i++) {
			String currentGroupName = handler.get(i).getGroupName();
			Boolean isGroupCompleted = true;
			
			// For every statement in the current group
			if (null != handler.get(i).getStatementHelperList()) {
				for (StatementHelperContainer currentStatement : handler.get(i)
						.getStatementHelperList()) {
					if (currentStatement.isStatementSwitched()) {
						if (null != currentStatement.isStatementValid()) {
							isGroupCompleted = isGroupCompleted && true;
						} else {
							isGroupCompleted = isGroupCompleted && false;
						}
					}else{
						//In the case where the statement is not switched
						isGroupCompleted = isGroupCompleted && currentStatement.isStatementValid();
					}
				}
				if (!isGroupCompleted) {
					toReturn.add(currentGroupName);
				}
			}
		}
		return toReturn;
	}

	private List<String> getGroupsValid() {
		List<String> toReturn = new ArrayList<String>();

		List<GroupHandlerHelper> handler = this.groupProcessor
				.getGroupsHandler();

		// Loop for every group
		for (int i = 0; i < handler.size(); i++) {
			String currentGroupName = handler.get(i).getGroupName();

			if (null != handler.get(i).getGroupValid()
					&& handler.get(i).getGroupValid()) {
				toReturn.add(currentGroupName);
			} else {
				Boolean isGroupValid = true;
				// Loop for every statement in the current group
				for (StatementHelperContainer currentStatement : handler.get(i)
						.getStatementHelperList()) {
					if (currentStatement.isStatementSwitched()) {
						if (null != currentStatement.isStatementValid()) {
							isGroupValid = isGroupValid
									&& currentStatement.isStatementValid();
						} else {
							isGroupValid = false;
						}

					}
				}
				// The group is valid iff all the statement it contains are
				// valid
				if (isGroupValid) {
					toReturn.add(currentGroupName);
				}
			}
		}
		return toReturn;
	}

	private List<String> getGroupsWithError() {
		List<GroupHandlerHelper> handler = this.groupProcessor
				.getGroupsHandler();

		List<String> toReturn = new ArrayList<String>();

		// Loop for every group
		for (int i = 0; i < handler.size(); i++) {
			String currentGroupName = handler.get(i).getGroupName();
			Boolean isGroupInError = false;
			// Loop for every statement in the current group
			if (null != handler.get(i).getStatementHelperList()) {
				for (StatementHelperContainer currentStatement : handler.get(i)
						.getStatementHelperList()) {
					if (currentStatement.isStatementSwitched()) {
						if (null != currentStatement.isStatementValid()) {
							isGroupInError = isGroupInError
									|| !currentStatement.isStatementValid();
						}

					}
				}
				// The group is in error if at least one of statement is in
				// error
				if (isGroupInError) {
					toReturn.add(currentGroupName);
				}
			}

		}
		return toReturn;
	}
}
