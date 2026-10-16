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
abstract class CharArrayPersistIdModel
{
  private final char[] _id;

  CharArrayPersistIdModel( final char[] id )
  {
    _id = id;
  }

  @PersistId
  char[] getId()
  {
    return _id;
  }

  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int value );
}
