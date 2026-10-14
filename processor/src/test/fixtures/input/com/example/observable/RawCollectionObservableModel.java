package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "rawtypes" )
@ArezComponent
abstract class RawCollectionObservableModel
{
  @Observable
  public abstract @Nullable List getMyValue();

  public abstract void setMyValue( final @Nullable List value );
}
