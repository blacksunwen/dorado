package com.bstek.dorado.vidorsupport.internal.vidor;

import org.codehaus.jackson.JsonNode;

public class JsonException extends IllegalArgumentException {

	private static final long serialVersionUID = 4592010488237374967L;

	public JsonException(JsonNode jsonNode) {
		super("json fragment not correct" + " [" + jsonNode.asText() + "]");
	}

}
