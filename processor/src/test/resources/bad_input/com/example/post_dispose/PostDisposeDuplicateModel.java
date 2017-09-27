package com.example.post_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeDuplicateModel
{
  @PostDispose
  void foo()
  {
  }

  @PostDispose
  void doStuff()
  {
  }
}
