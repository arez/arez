package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class GenericObservableModel
{
  @NullMarked
  public interface MyValue<T extends @Nullable Object>
  {
  }

  @Observable
  public @Nullable MyValue<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final @Nullable MyValue<String> time )
  {
  }
}
