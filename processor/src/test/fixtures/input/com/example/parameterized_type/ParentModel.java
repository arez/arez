package com.example.parameterized_type;

import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class ParentModel<T extends Number>
{
  private @Nullable T _value;

  @Observable
  public @Nullable T getValue()
  {
    return _value;
  }

  public void setValue( final @Nullable T value )
  {
    _value = value;
  }
}
