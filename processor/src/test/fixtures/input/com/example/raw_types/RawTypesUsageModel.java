package com.example.raw_types;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// Example of usage of raw type through different
// arez annotated methods should cause problems
@NullMarked
@SuppressWarnings( "rawtypes" )
@ArezComponent
public abstract class RawTypesUsageModel
{
  @Action
  public void doStuff( @NonNull final Callable callable )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract @Nullable Callable getMyCallable();

  abstract void setMyCallable( @Nullable Callable callable );

  @Observable
  @Nullable Callable getMyCallable2()
  {
    return null;
  }

  void setMyCallable2( @Nullable Callable callable )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract @Nullable List<Callable> getMyCallableList();

  abstract void setMyCallableList( @Nullable List<Callable> callable );

  @Observe( executor = Executor.EXTERNAL )
  public void render( @NonNull final Callable callable )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @Memoize
  @Nullable Callable genCallable()
  {
    return null;
  }

  @Memoize
  int genCallableStat1( @Nullable Callable callable )
  {
    return 0;
  }

  @Memoize
  int genCallableStat2( @Nullable List<Consumer<Callable>> other )
  {
    return 0;
  }
}
