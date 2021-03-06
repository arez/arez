package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class ThrowsMethodComponent
{
  @SuppressWarnings( { "RedundantThrows" } )
  @CascadeDispose
  final Disposable myField()
    throws Exception
  {
    return null;
  }
}
