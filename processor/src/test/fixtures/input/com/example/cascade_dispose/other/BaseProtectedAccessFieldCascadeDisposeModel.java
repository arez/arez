package com.example.cascade_dispose.other;

import arez.Disposable;
import arez.annotations.CascadeDispose;

public abstract class BaseProtectedAccessFieldCascadeDisposeModel
{
  @CascadeDispose
  protected Disposable _myElement;
}
