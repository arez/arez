package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( requireEquals = Feature.DISABLE )
public abstract class NoRequireEqualsModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
