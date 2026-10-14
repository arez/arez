package com.example.inheritance.other;

import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentId;
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
public abstract class BaseCompleteModel
{
  @CascadeDispose
  protected @Nullable Disposable _myDisposableField;

  @PreDispose
  protected final void preDispose()
  {
  }

  @PostDispose
  protected final void postDispose()
  {
  }

  @Action
  public void myAction()
  {
  }

  @Observe
  protected void myWatcher()
  {
  }

  @ObserverRef
  protected abstract @Nullable Observer getMyWatcherObserver();

  @ComponentId
  protected final byte getId()
  {
    return 0;
  }

  @ComponentNameRef
  protected abstract @Nullable String getComponentName();

  @NonNull
  @ComponentRef
  protected abstract Component getComponent();

  @Memoize
  protected long getTime()
  {
    return 0;
  }

  @Memoize
  protected long calcStuff( int i )
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  protected abstract ComputableValue<Long> getTimeComputableValue();

  @OnActivate
  protected final void onTimeActivate()
  {
  }

  @OnDeactivate
  protected final void onTimeDeactivate()
  {
  }

  @Memoize
  protected @Nullable Collection<Long> getCollectionTime()
  {
    return Collections.emptyList();
  }

  @Memoize
  protected @Nullable Collection<Long> calcCollectionStuff( int i )
  {
    return Collections.emptyList();
  }

  @ContextRef
  protected abstract @Nullable ArezContext getContext();

  @Observable
  protected abstract @Nullable String getMyValue();

  public abstract void setMyValue( @Nullable String value );

  @NonNull
  @ObservableValueRef
  protected abstract ObservableValue<String> getMyValueObservableValue();

  @Observable( setterAlwaysMutates = false )
  protected int getMyPrimitiveValue()
  {
    return 0;
  }

  protected void setMyPrimitiveValue( int value )
  {
  }

  @Observable( setterAlwaysMutates = false )
  protected @Nullable String getMyStringValue()
  {
    return null;
  }

  protected void setMyStringValue( @Nullable String value )
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  protected void onRenderDepsChange()
  {
  }

  @PostConstruct
  protected void postConstruct()
  {
  }

  @Reference
  protected abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  protected int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  public static class MyEntity
  {
  }

  @Observable
  @Inverse
  protected abstract @Nullable List<Element> getElements();
}
