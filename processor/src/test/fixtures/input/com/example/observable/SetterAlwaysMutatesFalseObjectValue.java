package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class SetterAlwaysMutatesFalseObjectValue
{
  private @Nullable String _field;

  public @Nullable String getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false )
  public void setField( @Nullable String field )
  {
    _field = field;
  }
}
