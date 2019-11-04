package com.example.package_access.other;

import arez.Disposable;
import arez.annotations.CascadeDispose;

public abstract class BaseCascadeDisposeModel
{
  @CascadeDispose
  Disposable _myDisposableField;
}
