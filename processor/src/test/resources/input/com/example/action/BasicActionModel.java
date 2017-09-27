package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class BasicActionModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
