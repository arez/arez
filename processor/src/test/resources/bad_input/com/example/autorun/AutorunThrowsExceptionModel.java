package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import java.text.ParseException;

@ArezComponent
public class AutorunThrowsExceptionModel
{
  @Autorun
  void doStuff()
    throws ParseException
  {
  }
}
