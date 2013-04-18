package org.purl.wf4ever.astrotaverna.pdl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.ivoa.parameter.model.ConditionalStatement;
import net.ivoa.parameter.model.ConstraintOnGroup;
import net.ivoa.parameter.model.ParameterGroup;
import net.ivoa.parameter.model.ParameterReference;
import net.ivoa.parameter.model.Service;
import net.ivoa.parameter.model.SingleParameter;
import net.ivoa.pdl.interpreter.conditionalStatement.StatementHelperContainer;
import net.ivoa.pdl.interpreter.expression.ExpressionParserFactory;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupHandlerHelper;
import net.ivoa.pdl.interpreter.groupInterpreter.GroupProcessor;
import net.ivoa.pdl.interpreter.utilities.UserMapper;
import net.ivoa.pdl.interpreter.utilities.Utilities;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;

import org.apache.log4j.Logger;

import visitors.GeneralParameterVisitor;
import CommonsObjects.GeneralParameter;

public class PDLServiceController {
	
	/*
	 * Best practice: Keep port names as constants to avoid misspelling. This
	 * would not apply if port names are looked up dynamically from the service
	 * operation, like done for WSDL services.
	 */
	
	private static Logger logger = Logger.getLogger(PDLServiceActivity.class);
	
	
	private PDLServiceActivityConfigurationBean configBean;
	private Service service;
	private GroupProcessor gp;
	
	private HashMap<String, SingleParameter> hashParameters= new HashMap<String, SingleParameter>();
	private HashMap<String, SingleParameter> hashInputParameters = new HashMap<String, SingleParameter>();
	private HashMap<String, SingleParameter> hashOutputParameters = new HashMap<String, SingleParameter>();
	private HashMap<String, String> restrictionsOnGroups;
	private String serviceDescription;
	
	//pdl specific objects
	final private String complete = "To complete";
	//final private String error = "With error";
	final private static String valid = "Valid";
	final private static String pending = "pending";
	final private static String finished = "finished";
	final private static String aborted = "aborted";
	final private static String error = "error";
	final private static String running = "running";
	final private static String completed = "COMPLETED";
	final private static String executing = "EXECUTING";
	
	
	
	
	public PDLServiceController(PDLServiceActivityConfigurationBean configBean) throws ActivityConfigurationException{
		this.configBean = configBean;

		service = buildService(configBean.getPdlDescriptionFile());
		Utilities.getInstance().setService(service);
		Utilities.getInstance().setMapper(new UserMapper());
		serviceDescription = service.getDescription();
	}

	/*
	 * It explore the service definition and retrieve the input and output parameters hash.
	 *  It initialize hashParameters, hashInputParameters and hashOutputParameters
	 */
	public void prepareHashParametersInputs(){
		//service.getInputs().getConstraintOnGroup().getConditionalStatement().
		List<SingleParameter> serviceParameters = service.getParameters().getParameter();
		
		List<ParameterReference> inputParamRefs = getParameterRefeferences(service.getInputs());
		List<ParameterReference> outputParamRefs = getParameterRefeferences(service.getOutputs());
		
		ArrayList<SingleParameter> inputParameters = getSubsetOfSingleParameter(serviceParameters, inputParamRefs);
		ArrayList<SingleParameter> outputParameters = getSubsetOfSingleParameter(serviceParameters, outputParamRefs);
		
		//Input ports
		hashInputParameters = new HashMap<String, SingleParameter>();
		hashParameters = new HashMap<String, SingleParameter>();
		for(SingleParameter param: inputParameters){
//			addInput(param.getName(), 0, true, null, String.class);
			hashInputParameters.put(param.getName(), param);
			hashParameters.put(param.getName(), param);
		}
		
		//Output ports
		hashOutputParameters = new HashMap<String, SingleParameter>();
		for(SingleParameter param: outputParameters){
			// Single value output port (depth 0)
//			addOutput(param.getName(), 0);
			hashOutputParameters.put(param.getName(), param);
			hashParameters.put(param.getName(), param);
		}
		
		
		// FIXME: Replace with your input and output port definitions
		/*
		//The following commented code is a not efficient way to extract the inputParameters
		gp = new GroupProcessor(service);
		//System.out.println(service.getInputs().getParameterRef().get(0).getParameterName());
		gp.process();
		List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
		//paramsLists = new ArrayList();
		//dimensions = new HashMap();
		hashParameters = new HashMap();
		for(GroupHandlerHelper ghh : groupsHandler){
			List<SingleParameter> paramsList = ghh.getSingleParamIntoThisGroup();
			for(SingleParameter param: paramsList){
				//The following code is commented to ignore the dimension value, due to 
				//taverna has a native way to handle grids/arrays/lists
				//int dimension = -1;
				//if(param.getDimension()!=null){
				//	try{
				//		String value = ExpressionParserFactory.getInstance()
				//		   .buildParser(param.getDimension()).parse().get(0).getValue();
				//		dimension = new Integer(value).intValue();
				//	} catch (Exception ex){
				//		logger.error("I couln't read the dimension value for "+ param.getName());
				//		dimension = -1;
				//	}
				//}
				//if(dimension > 1 ){
				//	addInput(param.getName(), 1, true, null, String.class);	
				//	//dimensions.put(param.getName(), new Integer(1));
				//}else{
				addInput(param.getName(), 0, true, null, String.class);
				//	//dimensions.put(param.getName(), new Integer(0));
				//}
				hashParameters.put(param.getName(), param);
			}
			//if(paramsList!=null && paramsLists.size()>0)
			//	paramsLists.add(paramsList);
				
		}
		*/

	}
	
	
	public void prepareRestrictions(){
		
		HashMap<String, String> inputRestrictions = getRestrictionsOnGroup(service.getInputs());
		HashMap<String, String> outputRestrictions = getRestrictionsOnGroup(service.getOutputs());
		restrictionsOnGroups = new HashMap<String,String>();
		if(inputRestrictions!=null){
			restrictionsOnGroups.putAll(inputRestrictions);
		}
		if(outputRestrictions!=null){
			restrictionsOnGroups.putAll(outputRestrictions);
		}
	}
	
	public void prepareProcess() throws ActivityConfigurationException{
		if(service == null){
			service = buildService(configBean.getPdlDescriptionFile());
			Utilities.getInstance().setService(service);
			Utilities.getInstance().setMapper(new UserMapper());
			serviceDescription = service.getDescription();
		}
		gp = new GroupProcessor(service);
		gp.process();
	}
	
	
	/**
	 * It returns a list of the ParameterReference objets contained in group and all its subgroups
	 * @param group 
	 * @return List of ParameterReference Objects in the group and the subgroups
	 */
	private List<ParameterReference> getParameterRefeferences(ParameterGroup group){
		
		List<ParameterReference> list =  group.getParameterRef();
		
		if(group.getParameterGroup()!=null)
			for(ParameterGroup subgroup: group.getParameterGroup()){
				List<ParameterReference> list2 = getParameterRefeferences(subgroup);
				if(list2!=null)
					list.addAll(list2);
			}
		if(list == null)
			list = new ArrayList<ParameterReference>();
		
		return list;
	}
	
	/**
	 * It receives a list of SingleParameter and return the sublist corresponding with the selection.
	 * @param set List of SingleParameter objects
	 * @param selection List of SingleParameter references that must be included in the subset
	 * @return subset of SingleParameter containing the selection
	 */
	private ArrayList<SingleParameter> getSubsetOfSingleParameter (List<SingleParameter> set, List<ParameterReference> selection){
		ArrayList<SingleParameter> subset = new ArrayList<SingleParameter>();
		if(selection!=null && set !=null)
			for(ParameterReference ref: selection){
				boolean found = false;				
				Iterator<SingleParameter> it = set.iterator(); 
				while(it.hasNext() && !found){
					SingleParameter param = it.next();
					if(param.getName().compareTo(ref.getParameterName())==0){
						found = true;
						subset.add(param);
					}
				}
			}
		
		return subset;
	}
	
	/**
	 * It receives a group and return a hashMap with 1) a list of comma separated parameter names and 2) a description 
	 * of a conditionalStatement that affects to the previous parameters.
	 * It returns the restrictions for the group and the subgroups.
	 * @param group ParameterGroup
	 * @return restrictions for each set of parameters
	 */
	private HashMap<String, String> getRestrictionsOnGroup(ParameterGroup group){
		
		HashMap<String, String> restrictions = new HashMap();

		List<ParameterReference> paramList =  group.getParameterRef();
		
		ConstraintOnGroup constraint = group.getConstraintOnGroup();
		if(paramList != null && constraint != null)
			if(constraint.getConditionalStatement() != null){
				String paramListString = "";
				Iterator<ParameterReference> it = paramList.iterator();
				while(it.hasNext()){
					paramListString += it.next().getParameterName();
					if(it.hasNext())
						paramListString += ", ";
				} 
				 
				String comments="";	
				int i=1;
				for(ConditionalStatement stmt: constraint.getConditionalStatement()){									
					if(stmt.getComment()!=null && !stmt.getComment().isEmpty()){
						comments +=i+") "+stmt.getComment()+"\n";
						i++;
					}
				}
				if(!comments.isEmpty())
					restrictions.put(paramListString, comments);
			}
		
		if(group.getParameterGroup()!=null)
			for(ParameterGroup subgroup: group.getParameterGroup()){
				HashMap<String, String> list2 = getRestrictionsOnGroup(subgroup);
				if(list2!=null)
					restrictions.putAll(list2);
			}

		return restrictions;
	}
	
	
	public HashMap<String, SingleParameter> getHashAllParameters() {
		return hashParameters;
	}

	public HashMap<String, SingleParameter> getHashInputParameters() {
		return hashInputParameters;
	}
	
	public HashMap<String, SingleParameter> getHashOutputParameters() {
		return hashOutputParameters;
	}
	
	public HashMap<String, String> getRestrictionsOnGroups() {
		return restrictionsOnGroups;
	}
	
	public String getServiceDescription() {
		return serviceDescription;
	}
	
	
	public PDLServiceActivityConfigurationBean getConfiguration() {
		return this.configBean;
	}

	public GroupProcessor getGroupProcessor(){
		return this.gp;
	}
	
	public static String getValidStatus(){
		return valid;
	}
	
	public static String getRunningStatus(){
		return running;
	}
	
	public static String getCompletedStatus(){
		return completed;
	}
	
	public static String getExecutingStatus(){
		return executing;
	}
	
	/**
	 * Collect the single parameters on the different groups and return 
	 * a list will all of them. It previously requires to run prepareProcess method. 
	 * @return A list of singleParameter in the groups. 
	 */
	public List<SingleParameter> getSingleParametersOnGroups() {
		List<SingleParameter> paramsList = new ArrayList<SingleParameter>();
		List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
		
		for(GroupHandlerHelper ghh : groupsHandler){
			List<SingleParameter> paramsListOnGroup = ghh.getSingleParamIntoThisGroup();
			paramsList.addAll(paramsListOnGroup);
		}
		
		return paramsList;
	}
	
	
	
	//It builds a service from a PDL description file (file system file or URL)
	private Service buildService(String pdlDescriptionFile) throws ActivityConfigurationException {
		Service service = null;
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance("net.ivoa.parameter.model");
			Unmarshaller u = jaxbContext.createUnmarshaller();
			File file = new File(pdlDescriptionFile);
			if(file.exists()){
				service = (Service) u.unmarshal(file);
			}else{
				try {
					URI uri = new URI(pdlDescriptionFile);
					service = (Service) u.unmarshal(uri.toURL());
				} catch (URISyntaxException e) {
					//e.printStackTrace();
					logger.error("File does not exist or invalid URI for the PDL description file. "+e.getMessage());
					throw new ActivityConfigurationException("File does not exist or invalid URI for the PDL description file.\n"+e.getMessage());
				} catch (MalformedURLException e) {
					//e.printStackTrace();
					logger.error("File does not exist or invalid URI for the PDL description file. "+e.getMessage());
					throw new ActivityConfigurationException("File does not exist or invalid URL for the PDL description file.\n"+e.getMessage());
				} catch (IllegalArgumentException e) {
		            //e.printStackTrace();
					logger.error("File does not exist or invalid URI for the PDL description file."+e.getMessage());
		            throw new ActivityConfigurationException("File does not exist or invalid URL for the PDL description file.\n"+e.getMessage());
		        }
			}
			
		} catch (JAXBException e) {
			//e.printStackTrace();
			logger.error("buildService could not create a jaxbContext. "+e.getMessage());
			e.printStackTrace();
			throw new ActivityConfigurationException("buildService could not create a jaxbContext.\n"+e.getMessage());
		}
		return service;
	}
	
	/*
	 * The dimension is the size of the array. minimun size is 1
	 */
	public static int getDimension(SingleParameter param){
		int dimension = -1;
		if(param.getDimension()!=null){
			try{
				String value = ExpressionParserFactory.getInstance()
				   .buildParser(param.getDimension()).parse().get(0).getValue();
				dimension = new Integer(value).intValue();
			} catch (Exception ex){
				logger.error("I couln't read the dimension value for "+ param.getName());
				dimension = -1;
			}
		}
		if(dimension > 1 )
			return dimension;
		else
			return 1;
	}
	
	/**
	 * This method updates UserParameter object considering the following taverna activity inputs. 
	 * @param inputs
	 * @throws ActivityConfigurationException 
	 */
	public void updateUserMapperWithInputs(HashMap inputValuesMap) throws ActivityConfigurationException{
		if(gp==null)
			this.prepareProcess();
		List<GroupHandlerHelper> groupsHandler = gp.getGroupsHandler();
		for(GroupHandlerHelper ghh : groupsHandler){
			List<SingleParameter> paramsList = ghh.getSingleParamIntoThisGroup();
			if(paramsList!=null && paramsList.size()>0)
				for(SingleParameter param : paramsList){
					if(inputValuesMap.get(param.getName())!=null){
						//dimension?
						int dimension= PDLServiceController.getDimension(param);
						//if depth is 0 && dimension==1 then generalParamList only has one element
						if(dimension==1){
							//String value = (String) referenceService.renderIdentifier(inputs.get(param.getName()), 
							//		String.class, context);
							String value = (String) inputValuesMap.get(param.getName());
							//System.out.println(param.getName() + ", "+value+" ( "+ param.getParameterType().toString());
							// put every input in the Mapper
							List<GeneralParameter> generalParamList = new ArrayList<GeneralParameter>();
							GeneralParameter gparam = new GeneralParameter(value, 
									param.getParameterType().toString(), param.getName(),
									new GeneralParameterVisitor());
							generalParamList.add(gparam);
							
							Utilities.getInstance().getMapper().getMap()
							  .put(param.getName(), generalParamList);
						}else{
							//if depth is 1 then generalParamList has several elements
							//and input port gets a list
							//List<String> values = (List<String>) referenceService.renderIdentifier(inputs.get(param.getName()), 
							//		String.class, context);
							List<String> values = (List<String>) inputValuesMap.get(param.getName());
							//TODO
							//check if values has the size than it is said in dimension??
							
							List<GeneralParameter> generalParamList = new ArrayList<GeneralParameter>();
							for(String value : values){
								// put every input in the Mapper
								
								GeneralParameter gparam = new GeneralParameter(value, 
										param.getParameterType().toString(), param.getName(),
										new GeneralParameterVisitor());
								generalParamList.add(gparam);
							}
							Utilities.getInstance().getMapper().getMap()
							  .put(param.getName(), generalParamList);
						}
					} // end if(inputs.get(param.getName())!=null){
			}//end for(List<SingleParameter> list : paramsLists){
		}
	}

	public static String getPendingStatus() {
		return pending;
	}

	public static String getFinishedStatus() {
		return finished;
	}
	
	public static String getErrorStatus(){
		return error;
	}
	
	public static String getAbortedStatus(){
		return aborted;
	}

	/**
	 * Get a ErrorSummary with the restrictions that are violated. 
	 * @return
	 * @throws ActivityConfigurationException
	 */
	private ErrorSummary getSummaryOfErrorPerJob() throws ActivityConfigurationException {
		if(gp==null)
			prepareProcess();
		
		List<GroupHandlerHelper> handler = gp.getGroupsHandler();
		
		ErrorSummary errorOnThisJob = new ErrorSummary();
		
		Map<String, List<StatementHelperContainer>> errorsPerGroup = new HashMap<String, List<StatementHelperContainer>>();
		
		// Loop for every group
		for (int i = 0; i < handler.size(); i++) {
			String currentGroupName = handler.get(i).getGroupName();
			Boolean isGroupInError = false;
			List<StatementHelperContainer> statementInErrorInThisGroup = new ArrayList<StatementHelperContainer>();
			// Loop for every statement in the current group
			if (null != handler.get(i).getStatementHelperList()) {
				for (StatementHelperContainer currentStatement : handler.get(i)
						.getStatementHelperList()) {
					if (currentStatement.isStatementSwitched()) {
						if (null != currentStatement.isStatementValid()) {
						
							isGroupInError = isGroupInError
									|| !currentStatement.isStatementValid();
							
							if (!currentStatement.isStatementValid()) {
								statementInErrorInThisGroup
								.add(currentStatement);
							}
						}
					
					}
				}
				// The group is in error if at least one of statement is in
				// error
				if (isGroupInError) {
					errorOnThisJob.setHasJobError(true);
				}
			}
			errorsPerGroup.put(currentGroupName, statementInErrorInThisGroup);
		}
		errorOnThisJob.setErrorsPerGroup(errorsPerGroup);
		return errorOnThisJob;
	}
	
	/**
	 * Get a list of the restrictions that have been violated. This method  
	 * @return a list of errors
	 * @throws ActivityConfigurationException 
	 */
	public List<String> buildErrorsList() throws ActivityConfigurationException {
	
		ErrorSummary errorsOnThisJob = getSummaryOfErrorPerJob();
		
		if (errorsOnThisJob.getHasJobError()) {
		
			List<String> toReturn = new ArrayList<String>();
			
			for (Entry<String, List<StatementHelperContainer>> entry : errorsOnThisJob.getErrorsPerGroup().entrySet()) {
				for (int i = 0; i < entry.getValue().size(); i++) {
					String tempString = "Error on group " + entry.getKey()
						+ ": the constraint '+"
						+ entry.getValue().get(i).getStatementComment()
						+ "' is violated";
					toReturn.add(tempString);
				}
			}
			return toReturn;
		} else {
			return null;
		}
	}
	
}
