package com.example.package_access.other;

import arez.Disposable;
import arez.annotations.CascadeDispose;

public abstract class BaseCascadeDisposeMethodModel
{
  @CascadeDispose
  final Disposable myDisposableField()
  {
    return null;
  }
}
