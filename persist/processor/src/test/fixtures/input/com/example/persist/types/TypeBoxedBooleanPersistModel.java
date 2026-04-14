package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedBooleanPersistModel
{
  @Observable
  @Persist
  public abstract Boolean getValue();

  public abstract void setValue( Boolean v );
}
