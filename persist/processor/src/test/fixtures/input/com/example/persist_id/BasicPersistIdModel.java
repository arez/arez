package com.example.persist_id;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistId;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType
@ArezComponent
abstract class BasicPersistIdModel
{
  @PersistId
  int getId()
  {
    return 0;
  }

  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
