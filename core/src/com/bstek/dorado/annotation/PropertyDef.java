/**
 * 
 */
package com.bstek.dorado.annotation;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-3
 */
public @interface PropertyDef {
	String label() default "";

	String description() default "";
}
