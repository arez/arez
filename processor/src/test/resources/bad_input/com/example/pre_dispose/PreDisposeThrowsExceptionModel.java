package com.example.pre_dispose;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class PreDisposeThrowsExceptionModel
{
  @PreDispose
  void doStuff()
    throws ParseException
  {
  }
}
