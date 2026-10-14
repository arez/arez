package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
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
  public abstract @Nullable String getValue2();

  public abstract void setValue2( @Nullable String v );

  @Observable
  @Persist( store = "b" )
  public abstract @Nullable Double getValue3();

  public abstract void setValue3( @Nullable Double v );

  @Observable
  @Persist
  public abstract @Nullable Double getValue4();

  public abstract void setValue4( @Nullable Double v );
}
