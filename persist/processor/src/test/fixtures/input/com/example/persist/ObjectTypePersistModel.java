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
abstract class ObjectTypePersistModel
{
  @Observable
  @Persist
  public abstract @Nullable Object getValue();

  public abstract void setValue( @Nullable Object v );
}
