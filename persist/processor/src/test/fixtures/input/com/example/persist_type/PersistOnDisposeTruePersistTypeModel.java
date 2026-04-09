package com.example.persist_type;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@PersistType( persistOnDispose = true )
@ArezComponent
abstract class PersistOnDisposeTruePersistTypeModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
