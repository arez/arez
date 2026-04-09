package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class ObjectTypePersistModel
{
  @Observable
  @Persist
  public abstract Object getValue();

  public abstract void setValue( Object v );
}
