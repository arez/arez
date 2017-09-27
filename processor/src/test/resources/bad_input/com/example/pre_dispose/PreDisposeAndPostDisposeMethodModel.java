package com.example.pre_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class PreDisposeAndPostDisposeMethodModel
{
  @PreDispose
  @PostDispose
  public long doStuff()
  {
    return 22;
  }
}
