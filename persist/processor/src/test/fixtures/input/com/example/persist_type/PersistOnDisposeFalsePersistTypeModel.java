package com.example.persist_type;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;

@SuppressWarnings( "DefaultAnnotationParam" )
@PersistType( persistOnDispose = false )
@ArezComponent
abstract class PersistOnDisposeFalsePersistTypeModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
