package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "rawtypes" )
@ArezComponent
abstract class RawObservableModel
{
  @NullMarked
  public interface MyValue<T extends @Nullable Object>
  {
  }

  @Observable
  public abstract @Nullable MyValue getMyValue();

  public abstract void setMyValue( final @Nullable MyValue time );
}
