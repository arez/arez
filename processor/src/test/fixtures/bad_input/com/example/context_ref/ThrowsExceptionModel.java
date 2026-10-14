package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ThrowsExceptionModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ContextRef
  abstract @Nullable ArezContext getContext()
    throws Exception;
}
