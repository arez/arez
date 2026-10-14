package arez.persist.runtime;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks JVM-only declarations for removal by the J2CL transpiler.
 * J2CL recognizes the simple annotation name; it does not require GWT support.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@interface GwtIncompatible {}
