package arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that the named Arez compiler warnings should be suppressed in the
 * annotated element (and in all program elements contained in the annotated
 * element).  Note that the set of warnings suppressed in a given element is
 * a superset of the warnings suppressed in all containing elements.  For
 * example, if you annotate a class to suppress one warning and annotate a
 * method to suppress another, both warnings will be suppressed in the method.
 *
 * <p>As a matter of style, programmers should always use this annotation
 * on the most deeply nested element where it is effective.  If you want to
 * suppress a warning in a particular method, you should annotate that
 * method rather than its class.</p>
 *
 * <p>This class may be used instead of {@link SuppressWarnings} when the compiler
 * is passed compiled classes. The {@link SuppressWarnings} has a source retention
 * policy and is thus not available when the files are already compiled and is thus
 * not useful when attempting to suppress warnings in already compiled code.</p>
 */
@Target( { ElementType.TYPE,
           ElementType.FIELD,
           ElementType.METHOD,
           ElementType.PARAMETER,
           ElementType.CONSTRUCTOR,
           ElementType.LOCAL_VARIABLE } )
@Retention( RetentionPolicy.CLASS )
public @interface SuppressArezWarnings
{
  /**
   * The set of warnings that are to be suppressed by the compiler in the
   * annotated element. Duplicate names are permitted.  The second and
   * successive occurrences of a name are ignored.
   *
   * @return the set of warnings to be suppressed
   */
  String[] value();
}
