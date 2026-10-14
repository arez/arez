package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class DuplicateNamePersistModel
{
  @Observable( name = "X" )
  @Persist( name = "X" )
  public abstract int getValue();

  @Observable( name = "X" )
  public abstract void setX( int v );

  @Observable
  @Persist( name = "X" )
  public abstract int getValue2();

  public abstract void setValue2( int v );
}
