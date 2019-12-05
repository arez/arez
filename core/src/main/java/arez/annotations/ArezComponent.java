package arez.annotations;

import arez.Arez;
import arez.component.DisposeNotifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation that marks classes or interfaces to be processed by Arez annotation processor.
 * Classes or interfaces with this annotation can contain {@link Observable} properties,
 * {@link Memoize} properties, {@link Observe} methods and {@link Action} methods.
 *
 * <p>The annotation controls the way that contained actions and observables are
 * named (if names are enabled in the system.</p>
 * <ul>
 * <li>The value returned by {@link #name()} indicates the type name for instances
 * of this object. If not specified it will default to the SimpleName of the class.
 * i.e. The class <tt>com.biz.models.MyModel</tt> will default to a name of
 * "MyModel".</li>
 * <li>The {@link #nameIncludesId()} method indicates whether the names in generated components
 * should include the id. If you expect only one instance of this component, it can be simpler
 * to elide the id.</li>
 * </ul>
 * <p>The name of any elements contained within the component follows the pattern
 * "<tt>[ArezComponent.name].[ArezComponent.id].[Element.name]</tt>". If the value of {@link #nameIncludesId()}
 * is false then the "<tt>[ArezComponent.id].</tt>" element of the name will be elided.</p>
 *
 * <p>The type that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must be a class or an interface</li>
 * <li>Must be abstract</li>
 * <li>Must not be final</li>
 * <li>Must not be a non-static nested class</li>
 * <li>Must have at least one method annotated with {@link Action}, {@link Observe}, {@link Memoize} or {@link Observable}</li>
 * </ul>
 *
 * <p>The annotation processor that handles this annotation will analyze all super classes and super
 * interfaces. This includes analysis of default methods on interfaces. So it is perfectly valid to
 * add annotations such as {@link Observable}, {@link Memoize}, {@link Action}, {@link PreDispose} and/or
 * {@link PostDispose} to default methods on implemented interfaces.</p>
 */
@Documented
@Target( ElementType.TYPE )
public @interface ArezComponent
{
  /**
   * Return the name of the type.
   * The value must conform to the requirements of a java identifier.
   *
   * @return the name of the type.
   */
  @Nonnull
  String name() default "<default>";

  /**
   * Return true if the name derived for component should include the id, false otherwise.
   * If the user does not specify a value for this parameter but does specify the <tt>javax.inject.Singleton</tt>
   * on the same type then the default value of this parameter is false, otherwise it is true.
   *
   * @return true to include the id in the component name, false otherwise.
   */
  boolean nameIncludesId() default true;

  /**
   * Return true if the component does not need to explicitly declare elements.
   * Otherwise if no elements (i.e. {@link Observable}s, {@link Action}s, {@link Observe}s etc) are defined
   * on a component it will generate an error.
   *
   * @return true if the component does not need to explicitly declare elements, false otherwise.
   */
  boolean allowEmpty() default false;

  /**
   * Return the enum to control whether the component should support being "observed" by
   * {@link arez.component.ComponentObservable#observe(Object)}.
   * {@link Feature#ENABLE} will force the implementation of the ComponentObservable interface,
   * {@link Feature#DISABLE} will result in not implementing the ComponentObservable interface and
   * {@link Feature#AUTODETECT} will cause the component to implement the interface if the component
   * is also annotated with the {@link Repository} annotation or if the {@link #disposeOnDeactivate()}
   * parameter is true.
   *
   * @return enum to control whether the component should support being "observed".
   */
  Feature observable() default Feature.AUTODETECT;

  /**
   * Return enum to control whether the component should support implement the {@link DisposeNotifier} interface.
   * This will result in the component invoking dispose listener callbacks during dispose operation
   * within the scope of the disposing transaction.
   *
   * <p>If the value of this parameter is {@link Feature#AUTODETECT} then the component will implement the
   * interface {@link DisposeNotifier} interface unless the component is annotated with
   * <tt>javax.inject.Singleton</tt>.</p>
   *
   * @return Return enum to control whether the component should implement DisposeNotifier.
   */
  Feature disposeNotifier() default Feature.AUTODETECT;

  /**
   * Return true if the component should dispose itself once it is no longer "observed".
   * By "observed" it means that the component will have {@link arez.component.ComponentObservable#observe(Object)}
   * called on it. This parameter MUST be false if {@link #observable()} has the value {@link Feature#DISABLE}.
   *
   * @return true if the component should dispose itself once it is no longer "observed".
   */
  boolean disposeOnDeactivate() default false;

  /**
   * Specify how the component is integrated into the injection framework.
   *
   * @return enum controlling the integration into the injection framework.
   * @see InjectMode
   */
  InjectMode inject() default InjectMode.AUTODETECT;

  /**
   * Indicate whether dagger artifacts should be generated to support injection.
   * {@link Feature#ENABLE} will force the generation of the artifacts, {@link Feature#DISABLE}
   * will result in no dagger artifacts and {@link Feature#AUTODETECT} will add a dagger
   * artifacts if the <code>dagger.Module</code> class is present on the classpath AND {@link #inject()}
   * does not resolve to {@link InjectMode#NONE}.
   *
   * @return enum controlling whether a dagger module should be generated for repository.
   */
  Feature dagger() default Feature.AUTODETECT;

  /**
   * Return true if an the generated component should NOT trigger scheduler at the end of the constructor.
   * This is useful if the component creator will trigger schedules manually at a later time.
   * This MUST be false if there is no observed methods annotated on the component.
   *
   * @return return true if an the generated component should NOT trigger scheduler at the end of the constructor.
   */
  boolean deferSchedule() default false;

  /**
   * Indicate whether a component requires that the {@link Object#hashCode()} and {@link Object#equals(Object)}
   * methods are implemented. These methods MUST be implemented if the {@link Repository} annotation is present
   * but may be implemented in other scenarios. {@link Feature#ENABLE} will force the generation of the methods,
   * {@link Feature#DISABLE} will not generate these methods and {@link Feature#AUTODETECT} will generated these
   * methods if the {@link Repository} annotation is present.
   *
   * @return enum controlling whether the {@link Object#hashCode()} and {@link Object#equals(Object)} methods are implemented.
   */
  Feature requireEquals() default Feature.AUTODETECT;

  /**
   * Indicates whether the component should support access of the id via {@link arez.component.Identifiable#getArezId(Object)}.
   * This feature must be enabled and will be enabled when the value of the parameter is {@link Feature#AUTODETECT}
   * in the following scenarios:
   *
   * <ul>
   * <li>the {@link Repository} annotation is present on the same type.</li>
   * <li>a method annotated with the {@link Inverse} annotation is present.</li>
   * <li>a method annotated with the {@link ComponentId} annotation is present.</li>
   * <li>a method annotated with the {@link ComponentIdRef} annotation is present.</li>
   * </ul>
   *
   * <p>The feature is also enabled on every components if
   * {@link Arez#areNativeComponentsEnabled()} is true, {@link Arez#areRegistriesEnabled()} is true or
   * {@link Arez#areNamesEnabled()} is true and {@link #nameIncludesId()} is true.</p>
   *
   * @return enum controlling whether a unique if of the component can be accessed via {@link arez.component.Identifiable#getArezId(Object)}.
   */
  Feature requireId() default Feature.AUTODETECT;

  /**
   * Indicates whether the component should implement the interface {@link arez.component.Verifiable}.
   * This feature is ignored unless {@link Arez#isVerifyEnabled()} is true. The {@link Feature#AUTODETECT}
   * value will enable this feature if the component has any {@link Reference} methods or any {@link Inverse} methods.
   *
   * @return enum that indicates whether the component should implement the interface {@link arez.component.Verifiable}.
   */
  Feature verify() default Feature.AUTODETECT;

  /**
   * Indicate whether references to the component should also be annotated with {@link CascadeDispose} or {@link ComponentDependency}.
   * This is used to ensure that when a component is disposed that any reference to the component from another
   * component is rectified. The annotation processor will warn if the above rules are violated.
   * {@link Feature#ENABLE} will tell the annotation to warn if references to component are invalid.
   * {@link Feature#DISABLE} disables the warning. {@link Feature#AUTODETECT} will enable the warning if
   * the {@link #disposeNotifier()} resolves to {@link Feature#ENABLE}.
   *
   * @return enum controlling whether a references to components should be explicitly managed.
   */
  Feature verifyReferencesToComponent() default Feature.AUTODETECT;

  /**
   * The default priority used by {@link Memoize} and {@link Observe} annotated methods.
   * This parameter should not be specified only be specified
   * if there are {@link Memoize} or {@link Observe} annotated methods present on the component.
   *
   * @return the default priority used by {@link Memoize} and {@link Observe} annotated methods.
   */
  Priority defaultPriority() default Priority.NORMAL;
}
