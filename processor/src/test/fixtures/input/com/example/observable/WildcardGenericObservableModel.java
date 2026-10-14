package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class WildcardGenericObservableModel<T extends @Nullable Object>
{
  @NullMarked
  public interface MyValue<T extends @Nullable Object>
  {
  }

  @Observable
  public @Nullable MyValue<T> getMyValue()
  {
    return null;
  }

  public void setMyValue( final @Nullable MyValue<T> time )
  {
  }
}
