package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class SetterPersistModel
{
  public abstract int getValue();

  @Observable
  @Persist
  public abstract void setValue( int v );
}
