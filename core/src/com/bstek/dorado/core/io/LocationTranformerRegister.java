/**
 * 
 */
package com.bstek.dorado.core.io;

import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-22
 */
public class LocationTranformerRegister implements InitializingBean {
	private String protocal;
	private LocationTransformer transformer;

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public void setTransformer(LocationTransformer transformer) {
		this.transformer = transformer;
	}

	public void afterPropertiesSet() throws Exception {
		LocationTransformerHolder.getPathTransformers().put(protocal,
				transformer);
	}
}
