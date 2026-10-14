package com.example.cascade_dispose.other;

import arez.Disposable;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessFieldCascadeDisposeModel
{
  @CascadeDispose
  protected @Nullable Disposable _myElement;
}
