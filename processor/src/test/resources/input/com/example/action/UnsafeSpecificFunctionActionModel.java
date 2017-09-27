package com.example.action;

import java.text.ParseException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

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
