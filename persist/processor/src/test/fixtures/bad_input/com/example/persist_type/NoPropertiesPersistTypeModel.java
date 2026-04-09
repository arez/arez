package com.example.persist_type;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class NoPropertiesPersistTypeModel
{
  @Observable
  public abstract int getValue();

  public abstract void setValue( int v );
}
