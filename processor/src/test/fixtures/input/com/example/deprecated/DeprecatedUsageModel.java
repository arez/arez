package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Feature;
import arez.annotations.Memoize;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import java.util.List;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@NullMarked
@ArezComponent
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedUsageModel
{
  @Action
  public void doStuff( @NonNull final MyDeprecatedEntity entity )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract @Nullable MyDeprecatedEntity getMyEntity();

  abstract void setMyEntity( @Nullable MyDeprecatedEntity entity );

  @Observable
  @Nullable MyDeprecatedEntity getMyEntity2()
  {
    return null;
  }

  void setMyEntity2( @Nullable MyDeprecatedEntity entity )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract @Nullable List<MyDeprecatedEntity> getMyEntityList();

  abstract void setMyEntityList( @Nullable List<MyDeprecatedEntity> entity );

  @Observe( executor = Executor.EXTERNAL )
  public void render( @NonNull final MyDeprecatedEntity entity )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @Memoize
  @Nullable MyDeprecatedEntity genEntity()
  {
    return null;
  }

  @Memoize
  int genEntityStat1( @Nullable MyDeprecatedEntity entity )
  {
    return 0;
  }

  @Memoize
  int genEntityStat2( @Nullable List<Consumer<MyDeprecatedEntity>> other )
  {
    return 0;
  }
}
