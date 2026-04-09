package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedBytePersistModel
{
  @Observable
  @Persist
  public abstract Byte getValue();

  public abstract void setValue( Byte v );
}
