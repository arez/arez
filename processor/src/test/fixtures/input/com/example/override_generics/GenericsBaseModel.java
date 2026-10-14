package com.example.override_generics;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class GenericsBaseModel<V extends @Nullable Object>
{
  @Action
  public void foo( final @Nullable V v )
  {
  }
}
