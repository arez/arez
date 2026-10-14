package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import java.text.ParseException;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
