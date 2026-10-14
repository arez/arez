package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BadType3MethodComponent
{
  // Class does not implement Disposable
  @CascadeDispose
  final @Nullable List<?> myField()
  {
    return null;
  }
}
