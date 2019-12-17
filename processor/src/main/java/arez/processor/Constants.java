package arez.processor;

final class Constants
{
  static final String SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME = "arez.annotations.SuppressArezWarnings";
  static final String ACTION_ANNOTATION_CLASSNAME = "arez.annotations.Action";
  static final String COMPONENT_ANNOTATION_CLASSNAME = "arez.annotations.ArezComponent";
  static final String ACT_AS_COMPONENT_ANNOTATION_CLASSNAME = "arez.annotations.ActAsComponent";
  static final String OBSERVE_ANNOTATION_CLASSNAME = "arez.annotations.Observe";
  static final String CASCADE_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.CascadeDispose";
  static final String COMPONENT_DEPENDENCY_ANNOTATION_CLASSNAME = "arez.annotations.ComponentDependency";
  static final String PER_INSTANCE_ANNOTATION_CLASSNAME = "arez.annotations.PerInstance";
  static final String REFERENCE_ANNOTATION_CLASSNAME = "arez.annotations.Reference";
  static final String INVERSE_ANNOTATION_CLASSNAME = "arez.annotations.Inverse";
  static final String REFERENCE_ID_ANNOTATION_CLASSNAME = "arez.annotations.ReferenceId";
  static final String COMPONENT_ID_ANNOTATION_CLASSNAME = "arez.annotations.ComponentId";
  static final String COMPONENT_ID_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentIdRef";
  static final String COMPONENT_NAME_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentNameRef";
  static final String COMPONENT_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentRef";
  static final String COMPONENT_STATE_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentStateRef";
  static final String COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentTypeNameRef";
  static final String COMPUTABLE_VALUE_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComputableValueRef";
  static final String CONTEXT_REF_ANNOTATION_CLASSNAME = "arez.annotations.ContextRef";
  static final String MEMOIZE_ANNOTATION_CLASSNAME = "arez.annotations.Memoize";
  static final String OBSERVABLE_ANNOTATION_CLASSNAME = "arez.annotations.Observable";
  static final String OBSERVABLE_VALUE_REF_ANNOTATION_CLASSNAME = "arez.annotations.ObservableValueRef";
  static final String OBSERVER_REF_ANNOTATION_CLASSNAME = "arez.annotations.ObserverRef";
  static final String ON_ACTIVATE_ANNOTATION_CLASSNAME = "arez.annotations.OnActivate";
  static final String ON_DEACTIVATE_ANNOTATION_CLASSNAME = "arez.annotations.OnDeactivate";
  static final String ON_DEPS_CHANGE_ANNOTATION_CLASSNAME = "arez.annotations.OnDepsChange";
  static final String POST_CONSTRUCT_ANNOTATION_CLASSNAME = "arez.annotations.PostConstruct";
  static final String POST_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.PostDispose";
  static final String PRE_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.PreDispose";
  static final String POST_INVERSE_ADD_ANNOTATION_CLASSNAME = "arez.annotations.PostInverseAdd";
  static final String PRE_INVERSE_REMOVE_ANNOTATION_CLASSNAME = "arez.annotations.PreInverseRemove";
  static final String REPOSITORY_ANNOTATION_CLASSNAME = "arez.annotations.Repository";
  static final String COMPUTABLE_VALUE_CLASSNAME = "arez.ComputableValue";
  static final String OBSERVER_CLASSNAME = "arez.Observer";
  static final String DISPOSABLE_CLASSNAME = "arez.Disposable";
  static final String DISPOSE_NOTIFIER_CLASSNAME = "arez.component.DisposeNotifier";
  static final String EJB_POST_CONSTRUCT_ANNOTATION_CLASSNAME = "javax.annotation.PostConstruct";
  static final String INJECT_ANNOTATION_CLASSNAME = "javax.inject.Inject";
  static final String SINGLETON_ANNOTATION_CLASSNAME = "javax.inject.Singleton";
  static final String SCOPE_ANNOTATION_CLASSNAME = "javax.inject.Scope";
  static final String DAGGER_MODULE_CLASSNAME = "dagger.Module";
  static final String NONNULL_ANNOTATION_CLASSNAME = "javax.annotation.Nonnull";
  static final String NULLABLE_ANNOTATION_CLASSNAME = "javax.annotation.Nullable";
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

  private Constants()
  {
  }
}
