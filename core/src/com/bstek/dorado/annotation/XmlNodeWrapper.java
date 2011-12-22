/**
 * 
 */
package com.bstek.dorado.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-11
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface XmlNodeWrapper {
	String nodeName();

	boolean fixed() default true;
}
