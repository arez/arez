package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class BadStorePersistModel
{
  @Observable
  @Persist( store = "*my-store*" )
  public abstract int getValue();

  public abstract void setValue( int v );
}
