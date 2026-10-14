package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.text.ParseException;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class UnsafeSpecificProcedureActionModel
{
  @Action
  public void doStuff( final long time )
    throws ParseException
  {
  }
}
