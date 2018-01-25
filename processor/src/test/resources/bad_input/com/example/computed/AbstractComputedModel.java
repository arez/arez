package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class AbstractComputedModel
{
  @Computed
  abstract long getField();
}
