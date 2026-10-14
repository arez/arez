package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class CustomStorePersistModel
{
  @Observable
  @Persist( store = "s" )
  public abstract int getValue();

  public abstract void setValue( int v );
}
