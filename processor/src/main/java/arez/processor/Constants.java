package arez.processor;

final class Constants
{
  static final String SUPPRESS_AREZ_WARNINGS_CLASSNAME = "arez.annotations.SuppressArezWarnings";
  static final String ACTION_CLASSNAME = "arez.annotations.Action";
  static final String COMPONENT_CLASSNAME = "arez.annotations.ArezComponent";
  static final String ACT_AS_COMPONENT_CLASSNAME = "arez.annotations.ActAsComponent";
  static final String OBSERVE_CLASSNAME = "arez.annotations.Observe";
  static final String CASCADE_DISPOSE_CLASSNAME = "arez.annotations.CascadeDispose";
  static final String COMPONENT_DEPENDENCY_CLASSNAME = "arez.annotations.ComponentDependency";
  static final String REFERENCE_CLASSNAME = "arez.annotations.Reference";
  static final String INVERSE_CLASSNAME = "arez.annotations.Inverse";
  static final String REFERENCE_ID_CLASSNAME = "arez.annotations.ReferenceId";
  static final String COMPONENT_ID_CLASSNAME = "arez.annotations.ComponentId";
  static final String COMPONENT_ID_REF_CLASSNAME = "arez.annotations.ComponentIdRef";
  static final String COMPONENT_NAME_REF_CLASSNAME = "arez.annotations.ComponentNameRef";
  static final String COMPONENT_REF_CLASSNAME = "arez.annotations.ComponentRef";
  static final String COMPONENT_STATE_REF_CLASSNAME = "arez.annotations.ComponentStateRef";
  static final String COMPONENT_TYPE_NAME_REF_CLASSNAME = "arez.annotations.ComponentTypeNameRef";
  static final String COMPUTABLE_VALUE_REF_CLASSNAME = "arez.annotations.ComputableValueRef";
  static final String CONTEXT_REF_CLASSNAME = "arez.annotations.ContextRef";
  static final String MEMOIZE_CLASSNAME = "arez.annotations.Memoize";
  static final String OBSERVABLE_CLASSNAME = "arez.annotations.Observable";
  static final String OBSERVABLE_VALUE_REF_CLASSNAME = "arez.annotations.ObservableValueRef";
  static final String OBSERVER_REF_CLASSNAME = "arez.annotations.ObserverRef";
  static final String ON_ACTIVATE_CLASSNAME = "arez.annotations.OnActivate";
  static final String ON_DEACTIVATE_CLASSNAME = "arez.annotations.OnDeactivate";
  static final String ON_DEPS_CHANGE_CLASSNAME = "arez.annotations.OnDepsChange";
  static final String POST_CONSTRUCT_CLASSNAME = "arez.annotations.PostConstruct";
  static final String POST_DISPOSE_CLASSNAME = "arez.annotations.PostDispose";
  static final String PRE_DISPOSE_CLASSNAME = "arez.annotations.PreDispose";
  static final String POST_INVERSE_ADD_CLASSNAME = "arez.annotations.PostInverseAdd";
  static final String PRE_INVERSE_REMOVE_CLASSNAME = "arez.annotations.PreInverseRemove";
  static final String REPOSITORY_CLASSNAME = "arez.annotations.Repository";
  static final String COMPUTABLE_VALUE_CLASSNAME = "arez.ComputableValue";
  static final String OBSERVER_CLASSNAME = "arez.Observer";
  static final String DISPOSABLE_CLASSNAME = "arez.Disposable";
  static final String DISPOSE_NOTIFIER_CLASSNAME = "arez.component.DisposeNotifier";
  static final String EJB_POST_CONSTRUCT_CLASSNAME = "javax.annotation.PostConstruct";
  static final String JAX_WS_ACTION_CLASSNAME = "javax.xml.ws.Action";
  static final String INJECT_CLASSNAME = "javax.inject.Inject";
  static final String SINGLETON_CLASSNAME = "javax.inject.Singleton";
  static final String SCOPE_CLASSNAME = "javax.inject.Scope";
  static final String DAGGER_MODULE_CLASSNAME = "dagger.Module";
  static final String SENTINEL = "<default>";
  static final String WARNING_PUBLIC_LIFECYCLE_METHOD = "Arez:PublicLifecycleMethod";
  static final String WARNING_PROTECTED_LIFECYCLE_METHOD = "Arez:ProtectedLifecycleMethod";
  static final String WARNING_PUBLIC_HOOK_METHOD = "Arez:PublicHookMethod";
  static final String WARNING_PROTECTED_HOOK_METHOD = "Arez:ProtectedHookMethod";
  static final String WARNING_PUBLIC_REF_METHOD = "Arez:PublicRefMethod";
  static final String WARNING_PROTECTED_REF_METHOD = "Arez:ProtectedRefMethod";
  static final String WARNING_UNMANAGED_COMPONENT_REFERENCE = "Arez:UnmanagedComponentReference";
  static final String WARNING_UNNECESSARY_DEFAULT_PRIORITY = "Arez:UnnecessaryDefaultPriority";
  static final String WARNING_UNNECESSARY_ALLOW_EMPTY = "Arez:UnnecessaryAllowEmpty";
  static final String WARNING_UNNECESSARY_DEFAULT = "Arez:UnnecessaryDefault";
  static final String WARNING_PROTECTED_CONSTRUCTOR = "Arez:ProtectedConstructor";

  private Constants()
  {
  }
}
