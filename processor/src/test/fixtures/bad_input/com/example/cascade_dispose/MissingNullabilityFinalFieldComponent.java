package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
@ArezComponent
abstract class MissingNullabilityFinalFieldComponent
{
  @CascadeDispose
  final Disposable _myElement = null;
}
