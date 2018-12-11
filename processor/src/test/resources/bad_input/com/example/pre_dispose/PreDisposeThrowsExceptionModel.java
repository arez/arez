package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import java.text.ParseException;

@ArezComponent
public abstract class PreDisposeThrowsExceptionModel
{
  @SuppressWarnings( { "RedundantThrows" } )
  @PreDispose
  void doStuff()
    throws ParseException
  {
  }
}
