package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeLongPersistModel
{
  @Observable
  @Persist
  public abstract long getValue();

  public abstract void setValue( long v );
}
