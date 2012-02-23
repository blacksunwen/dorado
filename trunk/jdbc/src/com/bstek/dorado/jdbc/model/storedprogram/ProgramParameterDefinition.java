package com.bstek.dorado.jdbc.model.storedprogram;

import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class ProgramParameterDefinition extends ObjectDefinition implements Operation {

	@Override
	public void execute(Object object, CreationContext context)
			throws Exception {
		ProgramParameter parameter = (ProgramParameter) this.create(context);
		StoredProgram sp = (StoredProgram)object;
		
		sp.addParameter(parameter);
	}

}
