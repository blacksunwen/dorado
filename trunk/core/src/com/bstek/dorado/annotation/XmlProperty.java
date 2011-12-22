package com.bstek.dorado.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface XmlProperty {
	String propertyName() default "";

	String propertyType() default "";

	boolean ignored() default false;

	boolean unsupported() default false;

	boolean attributeOnly() default false;

	ExpressionMode expressionMode() default ExpressionMode.DYNA;

	String parser() default "";

	boolean composite() default false;
}
