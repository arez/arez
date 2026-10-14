package com.example.context_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class BadTypeModel
{
  @ContextRef
  abstract @Nullable String getContext();
}
