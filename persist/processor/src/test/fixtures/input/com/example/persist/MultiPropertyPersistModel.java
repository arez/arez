package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class MultiPropertyPersistModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );

  @Observable
  @Persist
  public abstract String getValue2();

  public abstract void setValue2( String v );

  @Observable
  @Persist
  public abstract Double getValue3();

  public abstract void setValue3( Double v );
}
