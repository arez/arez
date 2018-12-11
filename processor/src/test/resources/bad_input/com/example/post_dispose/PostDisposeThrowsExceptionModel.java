package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import java.text.ParseException;

@ArezComponent
public abstract class PostDisposeThrowsExceptionModel
{
  @SuppressWarnings( "RedundantThrows" )
  @PostDispose
  void doStuff()
    throws ParseException
  {
  }
}
