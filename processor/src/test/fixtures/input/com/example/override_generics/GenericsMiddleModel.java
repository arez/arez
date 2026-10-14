package com.example.override_generics;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class GenericsMiddleModel<V extends Number>
  extends GenericsBaseModel<V>
{
  @Action
  @Override
  public void foo( final @Nullable V v )
  {
    super.foo( v );
  }
}
