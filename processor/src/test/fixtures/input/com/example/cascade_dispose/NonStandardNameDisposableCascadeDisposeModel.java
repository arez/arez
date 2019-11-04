package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class NonStandardNameDisposableCascadeDisposeModel
{
  @CascadeDispose
  protected Disposable $$_myElement$$;
}
