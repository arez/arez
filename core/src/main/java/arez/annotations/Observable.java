package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to methods that expose an Observable value in Arez.
 * Methods annotated with this either query state or mutate state. The query
 * method is expected to have 0 parameters and return a value and by default
 * is named with "get" or "is" prefixed to the property name. The mutation
 * method is expected to have a single parameter and return no value and by
 * default is named with "set" prefixed to property name. The setter or getter
 * can also be named matching the property name without the prefix.
 *
 * <p>Only one of the query or mutation method needs to be annotated with
 * this annotation if the other method follows the normal conventions. If
 * the other method does not conform to conventions, then you will need to
 * annotate the pair and specify a value for {@link #name()}.</p>
 *
 * <p>The method should only invoked within the scope of a transaction.
 * The mutation method requires that the transaction be READ_WRITE.</p>
 *
 * <p>The method that is annotated with @Observable must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>May be abstract but if abstract then the paired setter or getter must also be abstract</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Observable
{
  /**
   * Return the name of the Observable relative to the component. If not specified
   * will default to the name of the property by convention as described above.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Computed}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the Observable relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Set this to false if there is no setter method and the component is
   * expected to use {@link ObservableRef} to indicate when value has changed.
   *
   * @return true if there is expected to be a setter, false if there should be no setter.
   */
  boolean expectSetter() default true;

  /**
   * Indicate whether the generated component class should add a parameter to the constructor to initialize this property.
   * This parameter should only be set to {@link Feature#ENABLE} when the observable property is defined by a
   * pair of abstract methods. If set to {@link Feature#AUTODETECT} then an initializer will be added for an
   * observable property if it is defined by a pair of abstract methods and the values is annotated with the
   * {@link javax.annotation.Nonnull} annotation.
   *
   * <p>The initializer parameters will be added as additional parameters at the end of the parameter list in
   * the generated classes constructors. The initializers will be defined in the order that the observable
   * properties are declared. They properties be assigned after the parent constructor has been invoked.</p>
   *
   * @return flag controlling whether a parameter should be added to the constructor to initialize the property.
   */
  Feature initializer() default Feature.AUTODETECT;

  /**
   * Return true if the observable be read outside a transaction.
   * If the observable can be read outside a transaction then {@link arez.Observable#reportObserved()} will
   * only be invoked in a tracking transaction (i.e. when an {@link arez.Observer} created the transaction).
   * Thus {@link Action} annotated methods that only access observables that set the readOutsideTransaction
   * parameter to true and neither access nor modify other arez elements no longer need to be annotated with
   * {@link Action} annotations.
   *
   * @return true to allow reads outside a transaction, false to require a transaction to read observable.
   */
  boolean readOutsideTransaction() default false;
}
