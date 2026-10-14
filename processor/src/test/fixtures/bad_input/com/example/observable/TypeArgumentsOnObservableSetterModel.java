package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class TypeArgumentsOnObservableSetterModel
{
  public <T extends @Nullable Object> @Nullable T getField()
  {
    return null;
  }

  @Observable
  public <T extends @Nullable Object> void setField( final @Nullable T field )
  {
  }
}
