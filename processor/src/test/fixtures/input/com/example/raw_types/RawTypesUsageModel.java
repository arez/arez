package com.example.raw_types;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.annotations.Repository;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

// Example of usage of raw type through different
// arez annotated methods should cause problems
@SuppressWarnings( "rawtypes" )
@Repository
@ArezComponent
public abstract class RawTypesUsageModel
{
  @Action
  public void doStuff( @Nonnull final Callable callable )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract Callable getMyCallable();

  abstract void setMyCallable( Callable callable );

  @Observable
  Callable getMyCallable2()
  {
    return null;
  }

  void setMyCallable2( Callable callable )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract List<Callable> getMyCallableList();

  abstract void setMyCallableList( List<Callable> callable );

  @Observe( executor = Executor.EXTERNAL )
  public void render( @Nonnull final Callable callable )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @Memoize
  Callable genCallable()
  {
    return null;
  }

  @Memoize
  int genCallableStat1( Callable callable )
  {
    return 0;
  }

  @Memoize
  int genCallableStat2( List<Consumer<Callable>> other )
  {
    return 0;
  }
}
