package com.bstek.dorado.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ViewAttribute {

	boolean visible() default true;

	boolean ignored() default false;

	boolean output() default true;

	String outputter() default "";

	String defaultValue() default "#default";

	String referenceComponentName() default "";

	String enumValues() default "";

	boolean highlight() default false;

	String editor() default "";
}
