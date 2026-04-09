package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedIntegerPersistModel
{
  @Observable
  @Persist
  public abstract Integer getValue();

  public abstract void setValue( Integer v );
}
