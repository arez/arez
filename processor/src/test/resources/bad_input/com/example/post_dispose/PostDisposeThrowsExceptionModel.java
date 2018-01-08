package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import java.text.ParseException;

@ArezComponent
public class PostDisposeThrowsExceptionModel
{
  @PostDispose
  void doStuff()
    throws ParseException
  {
  }
}
