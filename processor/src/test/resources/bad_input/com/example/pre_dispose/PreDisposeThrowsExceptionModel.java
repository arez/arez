package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import java.text.ParseException;

@ArezComponent
public class PreDisposeThrowsExceptionModel
{
  @PreDispose
  void doStuff()
    throws ParseException
  {
  }
}
