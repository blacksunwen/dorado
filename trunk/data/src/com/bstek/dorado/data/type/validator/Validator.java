package com.bstek.dorado.data.type.validator;

import java.util.List;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-12
 */
public interface Validator {

	/**
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	List<ValidationMessage> validate(Object value) throws Exception;
}
