package com.example.persist_id;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistId;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@PersistType
@ArezComponent
abstract class NullableReferencePersistIdModel
{
  private final @Nullable Object _id;

  NullableReferencePersistIdModel( final @Nullable Object id )
  {
    _id = id;
  }

  @PersistId
  @Nullable Object getId()
  {
    return _id;
  }

  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int value );
}
