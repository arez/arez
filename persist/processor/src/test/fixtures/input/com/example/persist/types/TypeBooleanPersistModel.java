package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class TypeBooleanPersistModel
{
  @Observable
  @Persist
  public abstract boolean getValue();

  public abstract void setValue( boolean v );
}
