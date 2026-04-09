package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class CustomSetterPersistModel
{
  @Observable( name = "value" )
  @Persist( setterName = "_setValue" )
  public abstract int getValue();

  @Observable( name = "value" )
  public abstract void _setValue( int v );
}
