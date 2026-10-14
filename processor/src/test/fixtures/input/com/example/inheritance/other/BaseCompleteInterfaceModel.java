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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface BaseCompleteInterfaceModel
{
  @CascadeDispose
  default @Nullable Disposable myDisposableField()
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
  @Nullable Observer getMyWatcherObserver();

  @ComponentNameRef
  @Nullable String getComponentName();

  @NonNull
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

  @NonNull
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
  default @Nullable Collection<Long> getCollectionTime()
  {
    return Collections.emptyList();
  }

  @Memoize
  default @Nullable Collection<Long> calcCollectionStuff( int i )
  {
    return Collections.emptyList();
  }

  @ContextRef
  @Nullable ArezContext getContext();

  @Observable
  @Nullable String getMyValue();

  void setMyValue( @Nullable String value );

  @NonNull
  @ObservableValueRef
  ObservableValue<String> getMyValueObservableValue();

  @Observable
  default @Nullable List<String> getMyCollectionValue()
  {
    return null;
  }

  default void setMyCollectionValue( @Nullable List<String> value )
  {
  }

  @Observable
  @NonNull
  default List<String> getMyNonnullCollectionValue()
  {
    return null;
  }

  default void setMyNonnullCollectionValue( @NonNull List<String> value )
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
  default @Nullable String getMyStringValue()
  {
    return null;
  }

  default void setMyStringValue( @Nullable String value )
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
  default void onRender2DepsChange( @NonNull final Observer observer )
  {
  }

  @Observe
  default void render3()
  {
  }

  @OnDepsChange
  default void onRender3DepsChange( @NonNull final Observer observer )
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
  @Nullable MyEntity getMyEntity();

  @ReferenceId
  default int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  class MyEntity
  {
  }

  @Observable
  @Inverse
  @Nullable List<OtherElement> getOtherElements();
}
