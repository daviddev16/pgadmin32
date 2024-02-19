package com.daviddev16.core.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * DocumentationOnly apenas anota uma outra anotação como anotação de documentação, ou seja,
 * anotações que serão apenas utilizadas para indicar funcionalidades ou constantes em outros
 * atributos.
 * */
@Retention(CLASS)
@Target(TYPE)
public @interface DocumentationOnly {}
