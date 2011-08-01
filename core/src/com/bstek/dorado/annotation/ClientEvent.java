package com.bstek.dorado.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ClientEvent {
	public String name();

	public String[] signature() default "";

	public boolean output() default true;
}
