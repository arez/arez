package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.ComponentObservable;
import javax.annotation.Nullable;

@ArezComponent
abstract class BasicAutoObserveModel
{
  @AutoObserve
  final MyComponent _field = null;

  @AutoObserve
  MyComponent current()
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

  static class MyComponent
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }

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
