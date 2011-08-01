package com.bstek.dorado.view.output;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.common.event.ClientEvent;
import com.bstek.dorado.common.event.ClientEventRegisterInfo;
import com.bstek.dorado.common.event.ClientEventRegistry;
import com.bstek.dorado.common.event.ClientEventSupported;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-11
 */
public class ClientEventListenersOutputter implements VirtualPropertyOutputter {

	public void output(Object object, String property, OutputContext context)
			throws Exception {
		if (object instanceof ClientEventSupported) {
			JsonBuilder json = context.getJsonBuilder();
			json.escapeableKey(property).escapeableObject();
			ClientEventSupported ces = (ClientEventSupported) object;
			for (Map.Entry<String, List<ClientEvent>> entry : ces
					.getAllClientEventListeners().entrySet()) {
				String eventName = entry.getKey();
				List<ClientEvent> listeners = entry.getValue();
				if (listeners.isEmpty()) continue;

				ClientEventRegisterInfo eventInfo = ClientEventRegistry
						.getClientEventRegisterInfo(ces.getClass(), eventName);
				if (!eventInfo.isOutput()) continue;

				json.escapeableKey(eventName);
				if (listeners.size() == 1) {
					outputListener(eventInfo, listeners.get(0), context);
				}
				else {
					json.escapeableArray();
					for (ClientEvent listener : listeners) {
						outputListener(eventInfo, listener, context);
					}
					json.endArray();
				}
				json.endKey();
			}
			json.endObject().endKey();
		}
	}

	protected void outputListener(ClientEventRegisterInfo eventInfo,
			ClientEvent listener, OutputContext context) throws IOException {
		JsonBuilder json = context.getJsonBuilder();
		json.beginValue();
		Writer writer = context.getWriter();
		writer.append("(function(");
		String signature = StringUtils.join(eventInfo.getSignature(), ',');
		if (signature != null) {
			writer.append(signature);
		}
		writer.append("){\n").append(listener.getScript()).append("\n})");
		json.endValue();
	}

}
