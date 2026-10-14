package com.example.persist;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import arez.persist.StoreTypes;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class UnnecessaryCustomStorePersistModel
{
  @Observable
  @Persist( store = StoreTypes.APPLICATION )
  public abstract int getValue();

  public abstract void setValue( int v );
}
