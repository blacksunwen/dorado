package com.bstek.dorado.data.type.validator;

import java.util.List;

import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-12
 */
@XmlNode(nodeName = "Validator")
public interface Validator {
	List<ValidationMessage> validate(Object value) throws Exception;
}
