package com.example.persist_type;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@PersistType( persistOnDispose = true )
@ArezComponent
abstract class PersistOnDisposeTruePersistTypeModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
