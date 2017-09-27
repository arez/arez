package com.example.pre_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PreDispose;

@ArezComponent( disposable = false )
public class PreDisposeNotDisposableModel
{
  @PreDispose
  void doStuff()
  {
  }
}
