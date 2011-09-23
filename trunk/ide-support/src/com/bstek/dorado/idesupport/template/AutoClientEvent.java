package com.bstek.dorado.idesupport.template;

import com.bstek.dorado.common.event.ClientEventRegisterInfo;
import com.bstek.dorado.idesupport.model.ClientEvent;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-24
 */
public class AutoClientEvent extends ClientEvent {
	private ClientEventRegisterInfo clientEventRegisterInfo;

	public AutoClientEvent(ClientEventRegisterInfo clientEventRegisterInfo) {
		this.clientEventRegisterInfo = clientEventRegisterInfo;
	}

	public ClientEventRegisterInfo getClientEventRegisterInfo() {
		return clientEventRegisterInfo;
	}
}
