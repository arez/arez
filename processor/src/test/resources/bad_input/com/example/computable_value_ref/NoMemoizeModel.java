package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NoMemoizeModel
{
  @Nonnull
  @ComputableValueRef
  public abstract ComputableValue getTimeComputableValue();
}
