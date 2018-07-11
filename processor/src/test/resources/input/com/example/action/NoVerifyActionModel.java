package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class NoVerifyActionModel
{
  @Action( verifyRequired = false )
  public void doStuff1()
  {
  }

  @Action( verifyRequired = false )
  public void doStuff2()
    throws Throwable
  {
  }

  @Action( verifyRequired = false )
  public int doStuff3()
  {
    return 0;
  }

  @Action( verifyRequired = false )
  public int doStuff4()
    throws Throwable
  {
    return 0;
  }
}
