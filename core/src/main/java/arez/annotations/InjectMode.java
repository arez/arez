package arez.annotations;

/**
 * Enum that controls how the component interacts with the injection framework if at all.
 * Arez currently assumes that <a href="https://google.github.io/dagger/">Dagger2</a> is used
 * as the injection framework. The artifacts that are generated when injections is enable
 * vary based on the configuration of the component and whether the component has a method annotated
 * with {@link PostConstruct}. This is an extremely rough part of the toolkit which requires excessive
 * work by the user. It is likely that in the future all of this will be automated away.
 *
 * <ul>
 * <li>
 * A component configured with {@link #CONSUME} and either has no {@link PostConstruct} annotated method
 * and no {@link Observe} annotated methods and no {@link Memoize} with keep alive set to <code>true</code>
 * or only uses constructor injection will have the <code>javax.inject.Inject</code> annotation added to
 * the constructor.
 * </li>
 * <li>
 * A component configured with {@link #PROVIDE} and either has no {@link PostConstruct} annotated method
 * and no {@link Observe} annotated methods and no {@link Memoize} with keep alive set to <code>true</code>
 * will have the <code>javax.inject.Inject</code> annotation added to the constructor and a Dagger2 module
 * created named <code>[MyComponent]DaggerModule</code> which must be added to the desired component.
 * </li>
 * <li>
 * A component configured with {@link #CONSUME} and a {@link PostConstruct} annotated method and has
 * non-constructor based injection will have an interface named <code>[MyComponent]DaggerComponentExtension</code>
 * created that the desired component must extend. The extension has a method named <code>bind[MyComponent]()</code>
 * that must be invoked before any instances of the arez component can be created.
 * </li>
 * </ul>
 */
public enum InjectMode
{
  /**
   * InjectMode is disabled. The component can not be created by the injection framework.
   */
  NONE,
  /**
   * The component must be created and injected by the injection framework. The component MUST have a single
   * constructor otherwise the injection framework will not know how to create the component.
   */
  CONSUME,
  /**
   * This enum has the same characteristics as {@link #CONSUME} and may also be injected
   * into other components managed by the injection framework.
   */
  PROVIDE,
  /**
   * Allow the annotation processor to determine whether injection should be enabled.
   * If any fields or methods in the component or any parent type has an <code>javax.inject.Inject</code>
   * annotation OR the class has an annotation that is itself annotated with the <code>javax.inject.Scope</code>
   * annotation then the component will be assumed to be {@link #PROVIDE} otherwise the value {@link #NONE}
   * is assumed.
   */
  AUTODETECT
}
