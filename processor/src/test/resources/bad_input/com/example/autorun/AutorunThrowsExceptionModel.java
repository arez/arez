package com.example.autorun;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class AutorunThrowsExceptionModel
{
  @Autorun
  void doStuff()
    throws ParseException
  {
  }
}
