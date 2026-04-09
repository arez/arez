package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class MultiStorePersistModel
{
  @Observable
  @Persist( store = "a" )
  public abstract int getValue();

  public abstract void setValue( int v );

  @Observable
  @Persist( store = "a" )
  public abstract String getValue2();

  public abstract void setValue2( String v );

  @Observable
  @Persist( store = "b" )
  public abstract Double getValue3();

  public abstract void setValue3( Double v );

  @Observable
  @Persist
  public abstract Double getValue4();

  public abstract void setValue4( Double v );
}
