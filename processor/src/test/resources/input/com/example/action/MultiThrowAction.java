package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import java.io.IOException;
import java.text.ParseException;

@ArezComponent
public abstract class MultiThrowAction
{
  @Action
  void myAction()
    throws ParseException, IOException
  {
  }
}
