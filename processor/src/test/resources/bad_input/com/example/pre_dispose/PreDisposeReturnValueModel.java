package com.example.pre_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent
public class PreDisposeReturnValueModel
{
  @PreDispose
  int doStuff()
  {
    return 0;
  }
}
