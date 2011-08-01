package com.bstek.dorado.view.resolver;

import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;

public abstract class AbstractDirective extends Directive {
	protected static final Log logger = LogFactory
			.getLog(AbstractDirective.class);

	protected void processException(InternalContextAdapter contextAdapter,
			Writer writer, Exception e) {
		contextAdapter.put(VelocityExceptionDirective.EXCEPTION_ATTRIBUTE, e);
		logger.error(e, e);
	}

}