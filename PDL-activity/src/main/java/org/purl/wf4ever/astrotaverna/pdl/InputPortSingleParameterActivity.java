package org.purl.wf4ever.astrotaverna.pdl;

import java.io.IOException;
import java.util.Map;

import net.ivoa.parameter.model.SingleParameter;

/**
 * Interface for an activity such as {@link PDLActivity} that can provide {@link SingleParameter}s 
 * descriptions for its inputs.
 * 
 * @author Julian Garrido
 * 
 * @param <ActivityBeanType> The configuration bean type of the activity
 */
@SuppressWarnings("unchecked")
public interface InputPortSingleParameterActivity {

	/**
	 * Provides access to the SingleParameter for a given input port name.
	 * <br>
	 * This SingleParameter represents the Description for the parameter, including 
	 * SKOS concept, units, ...
	 * 
	 * @param portName
	 * @return the SingleParameter or null if the portName is not recognised.
	 * @throws IOException
	 * 
	 * @see SingleParameter
	 * @see #getSingleParameterForInputPorts()
	 * @see #getSingleParameterForOutputPort(String)
	 */
	public abstract SingleParameter getSingleParameterForInputPort(String portName)
			throws IOException;

	/**
	 * Return SingleParameter for a all input ports.
	 * <p>
	 * This SingleParameter represents the Description for the parameters, including
	 * SKOS concepts, units, precision. 
	 * WSDL.
	 * 
	 * @param portName
	 * @return A {@link Map} from portname to {@link SingleParameter}
	 * @throws IOException If the PDL or some of its dependencies could not be read
	 * 
	 * @see SingleParameter
	 * @see #getSingleParameterForInputPort(String)
	 * @see #getSingleParameterForOutputPorts()
	 */
	public abstract Map<String, SingleParameter> getSingleParametersForInputPorts()
			throws IOException;
}
