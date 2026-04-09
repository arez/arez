package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class CustomNamePersistModel
{
  @Observable( name = "v" )
  @Persist( name = "v" )
  public abstract int getValue();

  public abstract void setV( int v );
}
