package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class RequiresNewTxTypeActionModel
{
  @Action( requireNewTransaction = true )
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
