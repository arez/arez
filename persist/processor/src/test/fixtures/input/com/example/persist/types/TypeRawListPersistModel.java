package com.example.persist.types;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "rawtypes" )
@PersistType
@ArezComponent
abstract class TypeRawListPersistModel
{
  @Observable
  @Persist
  public abstract @Nullable List getValue();

  public abstract void setValue( @Nullable List v );
}
