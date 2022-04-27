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
 * </ul>
 * <p>The name of any elements contained within the component follows the pattern
 * "<tt>[ArezComponent.name].[ArezComponent.id].[Element.name]</tt>".</p>
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
@StingProvider( "[FlatEnclosingName]Arez_[SimpleName]" )
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
   * Return true if the component does not need to explicitly declare elements.
   * Otherwise if no elements (i.e. {@link Observable}s, {@link Action}s, {@link Observe}s etc) are defined
   * on a component it will generate an error.
   *
   * @return true if the component does not need to explicitly declare elements, false otherwise.
   */
  boolean allowEmpty() default false;

  /**
   * An enum controlling whether the component is treated like a service or an entity.
   * A service is expected to be a long running component that exists based on the functionality of
   * the application while an entity represents data within the application and may come and go based
   * on changes in the application data. Within arez, the only practical effect is to change the default
   * behaviour of other features. See the documentation for other parameters for further details.
   *
   * <p>If set as {@link Feature#AUTODETECT} or left as the default value, then Arez will assumes that if
   * the component is injected using dependency injection, then the component is a service. Arez detects whether
   * the type is annotated by the <a href="https://sting-ioc.github.io/">sting</a> annotations {@code sting.Named},
   * {@code sting.Typed} and {@code sting.Eager} or the jsr330 annotation {@code javax.inject.Named} or any
   * annotation that is annotated with {@code javax.inject.Scope} annotation. If such an annotation is found then
   * the component defaults to being treated like a service.</p>
   *
   * @return an enum controlling whether the component is treated like a service or an entity.
   */
  Feature service() default Feature.AUTODETECT;

  /**
   * The enum to control whether the component should support being "observed" by implementing the
   * {@link arez.component.ComponentObservable} interface and allowing invocation of the
   * {@link arez.component.ComponentObservable#observe(Object)} method. If unset or explicitly set
   * to {@link Feature#AUTODETECT} then the component will implement the interface if the
   * {@link #disposeOnDeactivate()} parameter is {@code true}.
   *
   * @return enum to control whether the component should support being "observed".
   */
  Feature observable() default Feature.AUTODETECT;

  /**
   * Return enum to control whether the component should support implement the {@link DisposeNotifier} interface.
   * This will result in the component invoking dispose listener callbacks during dispose operation
   * within the scope of the disposing transaction.
   *
   * <p>If the value of this parameter is {@link Feature#AUTODETECT} then the {@link DisposeNotifier} interface
   * is not implemented if the {@link #service()} resolves to {@link Feature#ENABLE}.</p>
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
   * Enum controlling whether sting integration is enabled. If enabled, the annotation processor will
   * add sting annotations to the generated component implementation. If the value of this parameter is
   * {@link Feature#AUTODETECT} then sting integration will be enabled if the {@link #service()} resolves
   * to {@link Feature#ENABLE} and the {@code sting.Injector} class is present on the classpath.
   *
   * @return an enum controlling whether a sting integration is enabled.
   */
  Feature sting() default Feature.AUTODETECT;

  /**
   * Indicate whether the {@link Object#hashCode()} and {@link Object#equals(Object)} methods need to be implemented.
   * If set to {@link Feature#AUTODETECT} then the methods will not be generated.
   *
   * @return an enum whether the {@link Object#hashCode()} and {@link Object#equals(Object)} methods need to be implemented.
   */
  Feature requireEquals() default Feature.AUTODETECT;

  /**
   * Indicates whether the component should support access of the id via {@link arez.component.Identifiable#getArezId(Object)}.
   * The {@link Feature#AUTODETECT} will be treated as {@link Feature#ENABLE}. This feature must be enabled in
   * the following scenarios:
   *
   * <ul>
   * <li>a method annotated with the {@link Inverse} annotation is present.</li>
   * <li>a method annotated with the {@link ComponentId} annotation is present.</li>
   * <li>a method annotated with the {@link ComponentIdRef} annotation is present.</li>
   * </ul>
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
   * This parameter should only be specified if there are {@link Memoize} or {@link Observe} annotated
   * methods present on the component.
   *
   * @return the default priority used by {@link Memoize} and {@link Observe} annotated methods.
   */
  Priority defaultPriority() default Priority.NORMAL;

  /**
   * The default value for the readOutsideTransaction parameter of {@link Memoize} and {@link Observable}
   * annotated methods.
   *
   * @return The flag indicating whether the default is to allow reads outside a transaction or to require a transaction to read observables and memoized values.
   */
  Feature defaultReadOutsideTransaction() default Feature.AUTODETECT;

  /**
   * The default value for the writeOutsideTransaction parameter of {@link Observable} annotated methods.
   *
   * @return The flag indicating whether the default is to allow writes outside a transaction or to require a transaction to write observables values.
   */
  Feature defaultWriteOutsideTransaction() default Feature.AUTODETECT;
}
