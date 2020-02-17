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
import java.util.Collection;
import java.util.Collections;
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

  @Memoize
  default Collection<Long> getCollectionTime()
  {
    return Collections.emptyList();
  }

  @Memoize
  default Collection<Long> calcCollectionStuff( int i )
  {
    return Collections.emptyList();
  }

  @ContextRef
  ArezContext getContext();

  @Observable
  String getMyValue();

  void setMyValue( String value );

  @Nonnull
  @ObservableValueRef
  ObservableValue<String> getMyValueObservableValue();

  @Observable
  default List<String> getMyCollectionValue()
  {
    return null;
  }

  default void setMyCollectionValue( List<String> value )
  {
  }

  @Observable
  @Nonnull
  default List<String> getMyNonnullCollectionValue()
  {
    return null;
  }

  default void setMyNonnullCollectionValue( @Nonnull List<String> value )
  {
  }

  @Observable( setterAlwaysMutates = false )
  default int getMyPrimitiveValue()
  {
    return 0;
  }

  default void setMyPrimitiveValue( int value )
  {
  }

  @Observable( setterAlwaysMutates = false )
  default String getMyStringValue()
  {
    return null;
  }

  default void setMyStringValue( String value )
  {
  }

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

  @Observe( executor = Executor.EXTERNAL )
  default void render4()
  {
  }

  @OnDepsChange
  default void onRender4DepsChange()
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
