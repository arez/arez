package com.example.persist_id;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistId;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class DuplicatePersistIdModel
{
  @PersistId
  int getId1()
  {
    return 0;
  }

  @PersistId
  int getId2()
  {
    return 0;
  }

  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
