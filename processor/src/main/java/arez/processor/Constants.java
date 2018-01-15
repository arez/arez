package arez.processor;

final class Constants
{
  static final String ACTION_ANNOTATION_CLASSNAME = "arez.annotations.Action";
  static final String COMPONENT_ANNOTATION_CLASSNAME = "arez.annotations.ArezComponent";
  static final String AUTORUN_ANNOTATION_CLASSNAME = "arez.annotations.Autorun";
  static final String COMPONENT_ID_ANNOTATION_CLASSNAME = "arez.annotations.ComponentId";
  static final String COMPONENT_NAME_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentNameRef";
  static final String COMPONENT_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentRef";
  static final String COMPONENT_TYPE_NAME_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComponentTypeNameRef";
  static final String COMPUTED_ANNOTATION_CLASSNAME = "arez.annotations.Computed";
  static final String COMPUTED_VALUE_REF_ANNOTATION_CLASSNAME = "arez.annotations.ComputedValueRef";
  static final String CONTEXT_REF_ANNOTATION_CLASSNAME = "arez.annotations.ContextRef";
  static final String MEMOIZE_ANNOTATION_CLASSNAME = "arez.annotations.Memoize";
  static final String OBSERVABLE_ANNOTATION_CLASSNAME = "arez.annotations.Observable";
  static final String OBSERVABLE_REF_ANNOTATION_CLASSNAME = "arez.annotations.ObservableRef";
  static final String OBSERVER_REF_ANNOTATION_CLASSNAME = "arez.annotations.ObserverRef";
  static final String ON_ACTIVATE_ANNOTATION_CLASSNAME = "arez.annotations.OnActivate";
  static final String ON_DEACTIVATE_ANNOTATION_CLASSNAME = "arez.annotations.OnDeactivate";
  static final String ON_DEPS_CHANGED_ANNOTATION_CLASSNAME = "arez.annotations.OnDepsChanged";
  static final String ON_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.OnDispose";
  static final String ON_STALE_ANNOTATION_CLASSNAME = "arez.annotations.OnStale";
  static final String POST_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.PostDispose";
  static final String PRE_DISPOSE_ANNOTATION_CLASSNAME = "arez.annotations.PreDispose";
  static final String REPOSITORY_ANNOTATION_CLASSNAME = "arez.annotations.Repository";
  static final String TRACK_ANNOTATION_CLASSNAME = "arez.annotations.Track";

  static final String POST_CONSTRUCT_ANNOTATION_CLASSNAME = "javax.annotation.PostConstruct";
  static final String INJECT_ANNOTATION_CLASSNAME = "javax.inject.Inject";
  static final String SCOPE_ANNOTATION_CLASSNAME = "javax.inject.Scope";
  static final String DAGGER_MODULE_CLASSNAME = "dagger.Module";

  private Constants()
  {
  }
}
