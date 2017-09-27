package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class UnsafeProcedureActionModel
{
  @Action
  public void doStuff( final long time )
    throws Exception
  {
  }
}
