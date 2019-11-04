package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public abstract class PostDisposePrivateModel
{
  @PostDispose
  private void doStuff()
  {
  }
}
