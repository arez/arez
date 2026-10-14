package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class PublicCtorModel
{
  public PublicCtorModel()
  {
  }

  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
