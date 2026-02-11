package arez.processor;

import javax.annotation.Nonnull;

final class Constants
{
  @Nonnull
  static final String SUPPRESS_AREZ_WARNINGS_CLASSNAME = "arez.annotations.SuppressArezWarnings";
  @Nonnull
  static final String ACTION_CLASSNAME = "arez.annotations.Action";
  @Nonnull
  static final String COMPONENT_CLASSNAME = "arez.annotations.ArezComponent";
  @Nonnull
  static final String ACT_AS_COMPONENT_CLASSNAME = "arez.annotations.ActAsComponent";
  @Nonnull
  static final String OBSERVE_CLASSNAME = "arez.annotations.Observe";
  @Nonnull
  static final String CASCADE_DISPOSE_CLASSNAME = "arez.annotations.CascadeDispose";
  @Nonnull
  static final String COMPONENT_DEPENDENCY_CLASSNAME = "arez.annotations.ComponentDependency";
  @Nonnull
  static final String REFERENCE_CLASSNAME = "arez.annotations.Reference";
  @Nonnull
  static final String INVERSE_CLASSNAME = "arez.annotations.Inverse";
  @Nonnull
  static final String REFERENCE_ID_CLASSNAME = "arez.annotations.ReferenceId";
  @Nonnull
  static final String COMPONENT_ID_CLASSNAME = "arez.annotations.ComponentId";
  @Nonnull
  static final String COMPONENT_ID_REF_CLASSNAME = "arez.annotations.ComponentIdRef";
  @Nonnull
  static final String COMPONENT_NAME_REF_CLASSNAME = "arez.annotations.ComponentNameRef";
  @Nonnull
  static final String COMPONENT_REF_CLASSNAME = "arez.annotations.ComponentRef";
  @Nonnull
  static final String COMPONENT_STATE_REF_CLASSNAME = "arez.annotations.ComponentStateRef";
  @Nonnull
  static final String COMPONENT_TYPE_NAME_REF_CLASSNAME = "arez.annotations.ComponentTypeNameRef";
  @Nonnull
  static final String COMPUTABLE_VALUE_REF_CLASSNAME = "arez.annotations.ComputableValueRef";
  @Nonnull
  static final String CONTEXT_REF_CLASSNAME = "arez.annotations.ContextRef";
  @Nonnull
  static final String MEMOIZE_CLASSNAME = "arez.annotations.Memoize";
  @Nonnull
  static final String MEMOIZE_CONTEXT_PARAMETER_CLASSNAME = "arez.annotations.MemoizeContextParameter";
  @Nonnull
  static final String OBSERVABLE_CLASSNAME = "arez.annotations.Observable";
  @Nonnull
  static final String OBSERVABLE_INITIAL_CLASSNAME = "arez.annotations.ObservableInitial";
  @Nonnull
  static final String OBSERVABLE_VALUE_REF_CLASSNAME = "arez.annotations.ObservableValueRef";
  @Nonnull
  static final String OBSERVER_REF_CLASSNAME = "arez.annotations.ObserverRef";
  @Nonnull
  static final String ON_ACTIVATE_CLASSNAME = "arez.annotations.OnActivate";
  @Nonnull
  static final String ON_DEACTIVATE_CLASSNAME = "arez.annotations.OnDeactivate";
  @Nonnull
  static final String ON_DEPS_CHANGE_CLASSNAME = "arez.annotations.OnDepsChange";
  @Nonnull
  static final String POST_CONSTRUCT_CLASSNAME = "arez.annotations.PostConstruct";
  @Nonnull
  static final String POST_DISPOSE_CLASSNAME = "arez.annotations.PostDispose";
  @Nonnull
  static final String PRE_DISPOSE_CLASSNAME = "arez.annotations.PreDispose";
  @Nonnull
  static final String POST_INVERSE_ADD_CLASSNAME = "arez.annotations.PostInverseAdd";
  @Nonnull
  static final String PRE_INVERSE_REMOVE_CLASSNAME = "arez.annotations.PreInverseRemove";
  @Nonnull
  static final String COMPUTABLE_VALUE_CLASSNAME = "arez.ComputableValue";
  @Nonnull
  static final String OBSERVER_CLASSNAME = "arez.Observer";
  @Nonnull
  static final String DISPOSABLE_CLASSNAME = "arez.Disposable";
  @Nonnull
  static final String DISPOSE_NOTIFIER_CLASSNAME = "arez.component.DisposeNotifier";
  @Nonnull
  static final String OBJECTS_EQUALS_COMPARATOR_CLASSNAME = "arez.ObjectsEqualsComparator";
  @Nonnull
  static final String OBJECTS_DEEP_EQUALS_COMPARATOR_CLASSNAME = "arez.ObjectsDeepEqualsComparator";
  @Nonnull
  static final String EJB_POST_CONSTRUCT_CLASSNAME = "javax.annotation.PostConstruct";
  @Nonnull
  static final String JAX_WS_ACTION_CLASSNAME = "javax.xml.ws.Action";
  @Nonnull
  static final String INJECT_CLASSNAME = "javax.inject.Inject";
  @Nonnull
  static final String STING_INJECTOR = "sting.Injector";
  @Nonnull
  static final String STING_CONTRIBUTE_TO = "sting.ContributeTo";
  @Nonnull
  static final String STING_NAMED = "sting.Named";
  @Nonnull
  static final String STING_EAGER = "sting.Eager";
  @Nonnull
  static final String STING_TYPED = "sting.Typed";
  @Nonnull
  static final String SENTINEL = "<default>";
  @Nonnull
  static final String WARNING_PUBLIC_LIFECYCLE_METHOD = "Arez:PublicLifecycleMethod";
  @Nonnull
  static final String WARNING_PUBLIC_HOOK_METHOD = "Arez:PublicHookMethod";
  @Nonnull
  static final String WARNING_PUBLIC_REF_METHOD = "Arez:PublicRefMethod";
  @Nonnull
  static final String WARNING_PROTECTED_METHOD = "Arez:ProtectedMethod";
  @Nonnull
  static final String WARNING_UNMANAGED_COMPONENT_REFERENCE = "Arez:UnmanagedComponentReference";
  @Nonnull
  static final String WARNING_UNNECESSARY_DEFAULT_PRIORITY = "Arez:UnnecessaryDefaultPriority";
  @Nonnull
  static final String WARNING_UNNECESSARY_ALLOW_EMPTY = "Arez:UnnecessaryAllowEmpty";
  @Nonnull
  static final String WARNING_UNNECESSARY_DEFAULT = "Arez:UnnecessaryDefault";
  @Nonnull
  static final String WARNING_PROTECTED_CONSTRUCTOR = "Arez:ProtectedConstructor";
  @Nonnull
  static final String WARNING_PUBLIC_CONSTRUCTOR = "Arez:PublicConstructor";
  @Nonnull
  static final String WARNING_EXTENDS_COMPONENT = "Arez:ExtendsComponent";
  @Nonnull
  static final String WARNING_FINAL_METHOD = "Arez:FinalMethod";

  private Constants()
  {
  }
}
