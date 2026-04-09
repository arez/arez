package arez.persist;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to observable properties that direct Arez to persist the property.
 */
@Documented
@Target( ElementType.METHOD )
public @interface Persist
{
  /**
   * Return the name used to persist the property.
   * If unspecified and the method is named according to javabeans getter conventions then the java bean property name
   * will be used, otherwise the name of the method will be used. It should be notes that during the code generation
   * the name will also be used to look up the setter used to restore the property. So if the property is named
   * {@code expanded} then the library will expect a setter method named {@code setExpanded}.
   *
   * <p>It should be noted that production mode persistent properties that are not persisted across
   * reloads will use synthetic keys as an optimization strategy.</p>
   *
   * @return the name used to persist the property.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * The key identifying the store where the observable data is stored.
   * The name of the store must comply with the requirements for a java identifier.
   *
   * @return the key identifying the store where the observable data is stored.
   */
  @Nonnull
  String store() default "<default>";

  /**
   * Return the name of the setter.
   * If unspecified the tool assumes that the setter method is named according to javabeans setter based on the derived
   * {@link #name()} of the property. So if the property is named {@code expanded} then the library will
   * derive a setter method named {@code setExpanded}.
   *
   * @return the name of the setter used to update value when restoring from a persisted store.
   */
  @Nonnull
  String setterName() default "<default>";
}
