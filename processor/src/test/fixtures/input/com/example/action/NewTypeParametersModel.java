package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NewTypeParametersModel
{
  // This fixture deliberately tests a method-local return type parameter.
  @SuppressWarnings("TypeParameterUnusedInFormals")
  @Action
  public <T extends Integer> @Nullable T doStuff()
  {
    return null;
  }
}
