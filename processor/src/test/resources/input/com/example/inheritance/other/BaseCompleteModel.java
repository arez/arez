package com.example.inheritance.other;

import arez.ArezContext;
import arez.Component;
import arez.Observer;
import arez.annotations.Action;
import arez.annotations.Autorun;
import arez.annotations.ComponentId;
import arez.annotations.ComponentNameRef;
import arez.annotations.ComponentRef;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import arez.annotations.ContextRef;
import arez.annotations.LocatorRef;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import arez.annotations.ObserverRef;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDepsChanged;
import arez.annotations.OnDispose;
import arez.annotations.OnStale;
import arez.annotations.PostConstruct;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Track;
import arez.component.Locator;
import javax.annotation.Nonnull;

public abstract class BaseCompleteModel
{
  @Action
  public void myAction()
  {
  }

  @Autorun
  protected void myAutorun()
  {
  }

  @ObserverRef
  protected abstract Observer getMyAutorunObserver();

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

  @Computed
  protected long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputedValueRef
  protected abstract arez.ComputedValue<Long> getTimeComputedValue();

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

  @OnDispose
  protected final void onTimeDispose()
  {
  }

  @ContextRef
  protected abstract ArezContext getContext();

  @Observable
  protected abstract String getMyValue();

  public abstract void setMyValue( String value );

  @Nonnull
  @ObservableRef
  protected abstract arez.Observable<String> getMyValueObservable();

  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected void onRenderDepsChanged()
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

  @LocatorRef
  protected abstract Locator getLocator();

  public static class MyEntity
  {
  }
}
