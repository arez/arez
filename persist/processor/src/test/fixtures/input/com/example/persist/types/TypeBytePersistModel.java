package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBytePersistModel
{
  @Observable
  @Persist
  public abstract byte getValue();

  public abstract void setValue( byte v );
}
