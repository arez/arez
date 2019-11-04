package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class PreDisposeAbstractModel
{
  @PreDispose
  abstract void doStuff();
}
