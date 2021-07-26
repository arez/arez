package arez.testng;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation applied to test methods to indicate whether the test method should be wrapped in an arez action.
 * The hook will first look at the method and then will look at the enclosing class
 * and then super classes until the annotation is found or the java.util.Object class
 * is reached.
 */
@Documented
@Target( { ElementType.TYPE, ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface ActionWrapper
{
  /**
   * A flag indicating whether the test method should be wrapped in an action.
   *
   * @return true if the test method should be wrapped in an action, false otherwise.
   */
  boolean enable();
}
