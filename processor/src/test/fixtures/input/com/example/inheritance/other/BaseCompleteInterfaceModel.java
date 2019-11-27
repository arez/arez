package com.example.inheritance.other;

import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentRef;
import arez.annotations.ComputableValueRef;
import arez.annotations.ContextRef;
import arez.annotations.Executor;
import arez.annotations.Inverse;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDepsChange;
import arez.annotations.PostConstruct;
import arez.annotations.PostDispose;
import arez.annotations.PreDispose;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import javax.annotation.Nonnull;

public interface BaseCompleteInterfaceModel
{
  @CascadeDispose
  default Disposable myDisposableField()
  {
    return null;
  }

  @PreDispose
  default void preDispose()
  {
  }

  @PostDispose
  default void postDispose()
  {
  }

  @Action
  default void myAction()
  {
  }

  @Observe
  default void myWatcher()
  {
  }

  @ObserverRef
  Observer getMyWatcherObserver();

  @ComponentNameRef
  String getComponentName();

  @Nonnull
  @ComponentRef
  Component getComponent();

  @Memoize
  default long getTime()
  {
    return 0;
  }

  @Memoize
  default long calcStuff( int i )
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  ComputableValue<Long> getTimeComputableValue();

  @OnActivate
  default void onTimeActivate()
  {
  }

  @OnDeactivate
  default void onTimeDeactivate()
  {
  }

  @ContextRef
  ArezContext getContext();

  @Observable
  String getMyValue();

  void setMyValue( String value );

  @Nonnull
  @ObservableValueRef
  ObservableValue<String> getMyValueObservableValue();

  @Observe( executor = Executor.EXTERNAL )
  default void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  default void onRenderDepsChange()
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  default void render2( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  default void onRender2DepsChange( @Nonnull final Observer observer )
  {
  }

  @Observe
  default void render3()
  {
  }

  @OnDepsChange
  default void onRender3DepsChange( @Nonnull final Observer observer )
  {
  }

  @PostConstruct
  default void postConstruct()
  {
  }

  @Reference
  MyEntity getMyEntity();

  @ReferenceId
  default int getMyEntityId()
  {
    return 0;
  }

  class MyEntity
  {
  }

  @Observable
  @Inverse
  List<OtherElement> getOtherElements();
}
