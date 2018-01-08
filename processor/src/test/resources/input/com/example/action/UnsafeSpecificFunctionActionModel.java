package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.text.ParseException;

@ArezComponent
public class UnsafeSpecificFunctionActionModel
{
  @Action
  public int doStuff( final long time )
    throws ParseException
  {
    return 0;
  }
}
