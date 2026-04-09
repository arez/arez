package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeFloatPersistModel
{
  @Observable
  @Persist
  public abstract float getValue();

  public abstract void setValue( float v );
}
