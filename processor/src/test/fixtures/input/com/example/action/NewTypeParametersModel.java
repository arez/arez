package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NewTypeParametersModel
{
  @Action
  public <T extends Integer> @Nullable T doStuff()
  {
    return null;
  }
}
