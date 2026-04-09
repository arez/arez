package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedDoublePersistModel
{
  @Observable
  @Persist
  public abstract Double getValue();

  public abstract void setValue( Double v );
}
