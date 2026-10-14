package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@PersistType
@ArezComponent
abstract class TypeBoxedShortPersistModel
{
  @Observable
  @Persist
  public abstract @Nullable Short getValue();

  public abstract void setValue( @Nullable Short v );
}
