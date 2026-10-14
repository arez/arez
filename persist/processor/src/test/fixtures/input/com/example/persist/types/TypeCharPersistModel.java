package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class TypeCharPersistModel
{
  @Observable
  @Persist
  public abstract char getValue();

  public abstract void setValue( char v );
}
