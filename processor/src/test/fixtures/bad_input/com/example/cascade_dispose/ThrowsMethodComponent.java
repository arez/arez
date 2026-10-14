package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ThrowsMethodComponent
{
  @SuppressWarnings( { "RedundantThrows" } )
  @CascadeDispose
  final @Nullable Disposable myField()
    throws Exception
  {
    return null;
  }
}
