package com.example.post_dispose;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeThrowsExceptionModel
{
  @PostDispose
  void doStuff()
    throws ParseException
  {
  }
}
