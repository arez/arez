package com.example.deprecation;

import arez.Observer;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDepsChange;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "RedundantSuppression" )
@ArezComponent
abstract class DeprecationModel
{
  @SuppressWarnings( "deprecation" )
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }

  @Observable
  @SuppressWarnings( "deprecation" )
  public long getValue()
  {
    return 0;
  }

  @SuppressWarnings( "deprecation" )
  void setValue( long time )
  {
  }

  @SuppressWarnings( "deprecation" )
  @Observe
  void myObserve()
  {
  }

  @SuppressWarnings( "deprecation" )
  @Observe( executor = Executor.EXTERNAL )
  void render()
  {
  }

  void onRenderDepsChange()
  {
  }

  @SuppressWarnings( "deprecation" )
  void render2()
  {
  }

  @OnDepsChange
  void onRender2DepsChange()
  {
  }

  @SuppressWarnings( "deprecation" )
  @ObserverRef
  abstract @Nullable Observer getRender2Observer();

  @SuppressWarnings( "deprecation" )
  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }

  @SuppressWarnings( "deprecation" )
  @Memoize
  public long getTime2()
  {
    return 0;
  }

  @SuppressWarnings( "deprecation" )
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @SuppressWarnings( "deprecation" )
  @OnActivate
  void onTimeActivate()
  {
  }

  @SuppressWarnings( "deprecation" )
  @OnDeactivate
  void onTimeDeactivate()
  {
  }

  @SuppressWarnings( "deprecation" )
  @MemoizeContextParameter( pattern = "time2" )
  @Nullable String captureMyContextVar()
  {
    return "";
  }

  @SuppressWarnings( "deprecation" )
  void pushMyContextVar( @Nullable String var )
  {
  }

  @SuppressWarnings( "deprecation" )
  void popMyContextVar( @Nullable String var )
  {
  }

  @SuppressWarnings( "deprecation" )
  @Reference( name = "Blah" )
  abstract @Nullable MyEntity getMyEntity();

  @SuppressWarnings( "deprecation" )
  @ReferenceId( name = "Blah" )
  int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
