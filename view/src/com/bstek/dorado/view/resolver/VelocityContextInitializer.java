/**
 * 
 */
package com.bstek.dorado.view.resolver;

import org.apache.velocity.context.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-14
 */
public interface VelocityContextInitializer {

	void initialContext(Context context);
}
