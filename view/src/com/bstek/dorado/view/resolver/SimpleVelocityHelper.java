package com.bstek.dorado.view.resolver;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-14
 */
public class SimpleVelocityHelper extends VelocityHelper {
	private VelocityEngine velocityEngine;

	public SimpleVelocityHelper(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	protected Context createContext() throws Exception {
		return new VelocityContext();
	}

	@Override
	public VelocityEngine getVelocityEngine() throws Exception {
		return velocityEngine;
	}

}
