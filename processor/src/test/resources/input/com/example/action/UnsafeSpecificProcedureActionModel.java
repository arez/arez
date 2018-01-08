package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.text.ParseException;

@ArezComponent
public class UnsafeSpecificProcedureActionModel
{
  @Action
  public void doStuff( final long time )
    throws ParseException
  {
  }
}
