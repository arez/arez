package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
@ArezComponent
abstract class TypeStringPersistModel
{
  @Observable
  @Persist
  public abstract String getValue();

  public abstract void setValue( String v );
}
