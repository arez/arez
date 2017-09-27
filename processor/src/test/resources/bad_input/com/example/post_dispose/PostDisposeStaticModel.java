package com.example.post_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeStaticModel
{
  @PostDispose
  static void doStuff()
  {
  }
}
