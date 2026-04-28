package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class ConflictingNullabilityFinalFieldComponent
{
  @CascadeDispose
  @Nullable
  @Nonnull
  final Disposable _myElement = null;
}
