package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class NoMemoizeModel
{
  @NonNull
  @ComputableValueRef
  public abstract ComputableValue<Long> getTimeComputableValue();
}
