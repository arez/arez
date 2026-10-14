package com.example.package_access.other;

import arez.Disposable;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseCascadeDisposeMethodModel
{
  @CascadeDispose
  final @Nullable Disposable myDisposableField()
  {
    return null;
  }
}
