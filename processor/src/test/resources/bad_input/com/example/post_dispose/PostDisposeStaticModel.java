package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeStaticModel
{
  @PostDispose
  static void doStuff()
  {
  }
}
