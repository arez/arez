package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BadType2Component
{
  // Class does not implement Disposable
  @CascadeDispose
  @Nullable String _myField;
}
