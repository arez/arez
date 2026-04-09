package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import java.util.List;

@SuppressWarnings( "rawtypes" )
@PersistType
@ArezComponent
abstract class TypeRawListPersistModel
{
  @Observable
  @Persist
  public abstract List getValue();

  public abstract void setValue( List v );
}
