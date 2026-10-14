package com.example.override_generics;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class GenericsModel
  extends GenericsMiddleModel<Integer>
{
  @Action
  @Override
  public void foo( final @Nullable Integer v )
  {
    super.foo( v );
  }
}
