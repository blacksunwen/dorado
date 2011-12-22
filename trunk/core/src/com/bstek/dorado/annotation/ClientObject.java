/**
 * 
 */
package com.bstek.dorado.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ClientObject {
	String outputter() default "";

	boolean usePrototype() default false;

	String prototype() default "";

	String shortTypeName() default "";

	EscapeMode escapeMode() default EscapeMode.AUTO;

	ClientProperty[] properties() default @ClientProperty;
}