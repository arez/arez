package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeIntPersistModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
