package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeBoxedLongPersistModel
{
  @Observable
  @Persist
  public abstract Long getValue();

  public abstract void setValue( Long v );
}
