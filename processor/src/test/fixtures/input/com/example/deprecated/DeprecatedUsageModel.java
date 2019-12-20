package com.example.deprecated;

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
import java.util.function.Consumer;
import javax.annotation.Nonnull;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@Repository
@ArezComponent
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedUsageModel
{
  @Action
  public void doStuff( @Nonnull final MyDeprecatedEntity entity )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract MyDeprecatedEntity getMyEntity();

  abstract void setMyEntity( MyDeprecatedEntity entity );

  @Observable
  MyDeprecatedEntity getMyEntity2()
  {
    return null;
  }

  void setMyEntity2( MyDeprecatedEntity entity )
  {
  }

  @Observable( initializer = Feature.ENABLE )
  abstract List<MyDeprecatedEntity> getMyEntityList();

  abstract void setMyEntityList( List<MyDeprecatedEntity> entity );

  @Observe( executor = Executor.EXTERNAL )
  public void render( @Nonnull final MyDeprecatedEntity entity )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @Memoize
  MyDeprecatedEntity genEntity()
  {
    return null;
  }

  @Memoize
  int genEntityStat1( MyDeprecatedEntity entity )
  {
    return 0;
  }

  @Memoize
  int genEntityStat2( List<Consumer<MyDeprecatedEntity>> other )
  {
    return 0;
  }
}
