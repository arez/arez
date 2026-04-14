package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import java.util.Date;

@PersistType
@ArezComponent
abstract class TypeDatePersistModel
{
  @Observable
  @Persist
  public abstract Date getValue();

  public abstract void setValue( Date v );
}
