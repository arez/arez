package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.ComponentObservable;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class BasicAutoObserveModel
{
  @AutoObserve
  @Nullable
  final MyComponent _field = null;

  @AutoObserve
  @Nullable MyComponent current()
  {
    return _field;
  }

  @AutoObserve
  @Observable
  @Nullable
  abstract MyComponent getSelected();

  abstract void setSelected( @Nullable MyComponent selected );

  @AutoObserve
  @Reference( load = LinkType.LAZY )
  @Nullable
  abstract MyEntity getEntity();

  @ReferenceId
  @Observable
  @Nullable
  abstract Integer getEntityId();

  abstract void setEntityId( @Nullable Integer id );

  @NullMarked
  static class MyComponent
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }

  @NullMarked
  static class MyEntity
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }
}
