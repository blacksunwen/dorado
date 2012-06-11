package com.bstek.dorado.common.event;

/**
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-6-11
 */
public interface DynaSignatureClientEvent extends ClientEvent {
	String getSignature();
}
