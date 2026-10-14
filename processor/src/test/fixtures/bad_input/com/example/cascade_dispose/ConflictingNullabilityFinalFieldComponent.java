package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ConflictingNullabilityFinalFieldComponent
{
  @CascadeDispose
  @Nullable
  @NonNull
  final Disposable _myElement = null;
}
