package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedFloatPersistModel
{
  @Observable
  @Persist
  public abstract Float getValue();

  public abstract void setValue( Float v );
}
