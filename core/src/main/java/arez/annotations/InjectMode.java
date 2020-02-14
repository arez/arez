package arez.annotations;

/**
 * Enum that controls how the component interacts with the injection framework if at all.
 *
 * <ul>
 * <li>
 * A component configured with {@link #PROVIDE} and either has no {@link PostConstruct} annotated method
 * and no {@link Observe} annotated methods and no {@link Memoize} with keep alive set to <code>true</code>
 * will have the <code>javax.inject.Inject</code> annotation added to the constructor and a Dagger2 module
 * created named <code>[MyComponent]DaggerModule</code> which must be added to the desired component.
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
   * constructor otherwise the injection framework will not know how to create the component. The component
   * may be injected into other components managed by the injection framework.
   */
  PROVIDE,
  /**
   * Allow the annotation processor to determine whether injection should be enabled.
   * If the class has an annotation that is itself annotated with the <code>javax.inject.Scope</code>
   * annotation then the component will be assumed to be {@link #PROVIDE} otherwise the value {@link #NONE}
   * is assumed.
   */
  AUTODETECT
}
