package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class BadNamePersistModel
{
  @Observable
  @Persist( name = "-bad-name-*" )
  public abstract int getValue();

  public abstract void setValue( int v );
}
