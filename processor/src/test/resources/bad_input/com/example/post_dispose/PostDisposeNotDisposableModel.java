package com.example.post_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent( disposable = false )
public class PostDisposeNotDisposableModel
{
  @PostDispose
  void doStuff()
  {
  }
}
