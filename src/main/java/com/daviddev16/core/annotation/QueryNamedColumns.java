package com.daviddev16.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DocumentationOnly
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface QueryNamedColumns {

	String[] resultColumns() default {};
	
}
