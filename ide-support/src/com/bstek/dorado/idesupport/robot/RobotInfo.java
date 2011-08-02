package com.bstek.dorado.idesupport.robot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RobotInfo {
	String viewObject() default "";

	String label() default "";
}
