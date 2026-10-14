package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import java.util.Date;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@PersistType
@ArezComponent
abstract class TypeDatePersistModel
{
  @Observable
  @Persist
  public abstract @Nullable Date getValue();

  public abstract void setValue( @Nullable Date v );
}
