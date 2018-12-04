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
import arez.annotations.Memoize;
import arez.annotations.ContextRef;
import arez.annotations.Executor;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDepsChange;
import arez.annotations.OnStale;
import arez.annotations.PostConstruct;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class BaseCompleteModel
{
  @CascadeDispose
  protected Disposable _myDisposableField;

  @Action
  public void myAction()
  {
  }

  @Observe
  protected void myWatcher()
  {
  }

  @ObserverRef
  protected abstract Observer getMyWatcherObserver();

  @ComponentId
  protected final byte getId()
  {
    return 0;
  }

  @ComponentNameRef
  protected abstract String getComponentName();

  @Nonnull
  @ComponentRef
  protected abstract Component getComponent();

  @Memoize
  protected long getTime()
  {
    return 0;
  }

  @Nonnull
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

  @OnStale
  protected final void onTimeStale()
  {
  }

  @ContextRef
  protected abstract ArezContext getContext();

  @Observable
  protected abstract String getMyValue();

  public abstract void setMyValue( String value );

  @Nonnull
  @ObservableValueRef
  protected abstract ObservableValue<String> getMyValueObservableValue();

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
  protected abstract MyEntity getMyEntity();

  @ReferenceId
  protected int getMyEntityId()
  {
    return 0;
  }

  public static class MyEntity
  {
  }

  @Observable
  @Inverse
  protected abstract List<Element> getElements();
}
