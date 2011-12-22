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
@Target(ElementType.METHOD)
@Inherited
public @interface ClientProperty {
	String propertyName() default "";

	boolean ignored() default false;

	String outputter() default "";

	String escapeValue() default "";

	boolean evaluateExpression() default true;
}
