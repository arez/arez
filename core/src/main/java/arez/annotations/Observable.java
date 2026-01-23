package arez.annotations;

import arez.ObservableValue;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to methods that expose an ObservableValue value in Arez.
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
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>May be abstract but if abstract then the paired setter or getter must also be abstract</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface Observable
{
  /**
   * Return the name of the ObservableValue relative to the component. If not specified
   * will default to the name of the property by convention as described above.
   * The value must conform to the requirements of a java identifier.
   * The name must also be unique across {@link Observable}s,
   * {@link Memoize}s and {@link Action}s within the scope of the
   * {@link ArezComponent} annotated element.
   *
   * @return the name of the ObservableValue relative to the component.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Set this to false if there is no setter method and the component is
   * expected to use {@link ObservableValueRef} to indicate when value has changed.
   *
   * @return true if there is expected to be a setter, false if there should be no setter.
   */
  boolean expectSetter() default true;

  /**
   * Indicate whether the generated component class should add a parameter to the constructor to initialize this property.
   * This parameter should only be set to {@link Feature#ENABLE} when the observable property is defined by a
   * pair of abstract methods. If set to {@link Feature#AUTODETECT} then an initializer will be added for an
   * observable property if it is defined by a pair of abstract methods and the values is annotated with the
   * {@link javax.annotation.Nonnull} annotation and it is not annotated by {@link Inverse}.
   * It is an error to set this parameter to {@link Feature#ENABLE} when the property has an
   * {@link ObservableInitial} annotation. If {@link ObservableInitial} is present and this parameter is
   * {@link Feature#AUTODETECT}, the initializer is treated as {@link Feature#DISABLE}.
   *
   * <p>The initializer parameters will be added as additional parameters at the end of the parameter list in
   * the generated classes constructors. The initializers will be defined in the order that the observable
   * properties are declared. They properties be assigned after the parent constructor has been invoked.</p>
   *
   * @return flag controlling whether a parameter should be added to the constructor to initialize the property.
   */
  Feature initializer() default Feature.AUTODETECT;

  /**
   * Indicate whether the observable can be read outside a transaction.
   * If the value is {@link Feature#AUTODETECT} then the value will be derived from the
   * {@link ArezComponent#defaultReadOutsideTransaction()} parameter on the {@link ArezComponent} annotation. If
   * the value is set to {@link Feature#ENABLE} then the observable can be read outside a transaction and the
   * {@link ObservableValue#reportObserved()} will only be invoked if the observables is accessed from within
   * a tracking transaction (i.e. when an {@link arez.Observer} or {@link arez.ComputableValue} creates the
   * transaction). Thus, {@link Action} annotated methods that only access observables that set the
   * readOutsideTransaction parameter to {@link Feature#ENABLE} and neither access nor modify other arez elements
   * no longer need to be annotated with {@link Action} annotations.
   *
   * @return flag that determines whether the observable allows reads outside a transaction, false to require a transaction to read the observable.
   */
  Feature readOutsideTransaction() default Feature.AUTODETECT;

  /**
   * Return true if the observable will create an action if the write occurs outside a transaction.
   *
   * @return true to allow writes to create an action if needed, false to require a transaction to write observable.
   */
  Feature writeOutsideTransaction() default Feature.AUTODETECT;

  /**
   * Return false if the setter should verify observable value has changed before propagating change.
   * In some scenarios, the setter method will modify the value before updating the observable or may
   * decide to abort the update. This setting will force the generated code to check the value of the
   * observable property after the setter and only invoke {@link ObservableValue#reportChanged()} if
   * a change has actually occurred.
   *
   * <p>This parameter should not be set to false if the associated setter is abstract. It is also
   * invalid to set this value to false if {@link #expectSetter()} is false.</p>
   *
   * @return false if the setter should verify observable value has changed before propagating change.
   */
  boolean setterAlwaysMutates() default true;
}
