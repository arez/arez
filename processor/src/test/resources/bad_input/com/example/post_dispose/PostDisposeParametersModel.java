package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public class PostDisposeParametersModel
{
  @PostDispose
  void doStuff( int i )
  {
  }
}
