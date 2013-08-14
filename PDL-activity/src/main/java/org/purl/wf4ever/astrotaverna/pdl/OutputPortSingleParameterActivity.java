package org.purl.wf4ever.astrotaverna.pdl;

import java.io.IOException;
import java.util.Map;

import net.ivoa.parameter.model.SingleParameter;

/**
 * Interface for an activity such as {@link PDLActivity} that can provide {@link SingleParameter}s for
 * it's outputs.
 * 
 * @author Julian Garrido
 * 
 * @param <ActivityBeanType> The configuration bean type of the activity
 */
public interface OutputPortSingleParameterActivity {
	/**
	 * Provides access to the SingleParameter for a given output port name.
	 * <br>
	 * This TSingleParameter represents the description for the parameters, including
	 * SKOS concepts, units, precision. 
	 * 
	 * @param portName
	 * @return the SingleParameter, or null if the portName is not recognised.
	 * @throws IOException
	 * 
	 * @see SingleParameter
	 * @see #getSingleParameterForOutputPorts()
	 * @see #getSingleParameterForInputPort(String)
	 */
	public abstract SingleParameter getSingleParameterForOutputPort(
			String portName) throws IOException;

	/**
	 * Return SingleParameter for a all output ports.
	 * <p>
	 * This SingleParameter represents the description for the parameters, including
	 * SKOS concepts, units, precision. 
	 * 
	 * @param portName
	 * @return A {@link Map} from portname to {@link SingleParameter}
	 * @throws IOException If the PDL or some of its dependencies could not be read
	 * 
	 * @see SingleParameter
	 * @see #getSingleParameterForOutputPort(String)
	 * @see #getSingleParametersForInputPorts()
	 */
	public abstract Map<String, SingleParameter> getSingleParametersForOutputPorts()
			throws IOException;
}
