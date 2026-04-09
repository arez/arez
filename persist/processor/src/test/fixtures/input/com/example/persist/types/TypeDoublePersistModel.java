package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeDoublePersistModel
{
  @Observable
  @Persist
  public abstract double getValue();

  public abstract void setValue( double v );
}
