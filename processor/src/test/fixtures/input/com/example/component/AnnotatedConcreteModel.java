package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent( allowConcrete = true )
public class AnnotatedConcreteModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
