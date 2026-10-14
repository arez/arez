package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( disposeNotifier = Feature.DISABLE )
abstract class DisposeNotifierDisabledCascadeDisposeModel
{
  @CascadeDispose
  @Nullable
  final Disposable _myObject = null;
}
