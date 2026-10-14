package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ActionTypeParametersModel<T extends Integer>
{
  @Action
  public @Nullable T doStuff()
  {
    return null;
  }
}
