package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class AbstractModel
{
  @Memoize
  abstract long getField();
}
