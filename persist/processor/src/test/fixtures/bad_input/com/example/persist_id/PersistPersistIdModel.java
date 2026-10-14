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
abstract class PersistPersistIdModel
{
  private int _value;

  @PersistId
  @Observable
  @Persist
  public int getValue()
  {
    return _value;
  }

  public void setValue( int v )
  {
    _value = v;
  }
}
