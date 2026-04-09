package com.example.persist_type;

import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType
abstract class NonArezComponentPersistTypeModel
{
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
