package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
public abstract class PrivateComponent
{
  @CascadeDispose
  private Disposable _myField;
}
