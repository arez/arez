package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent
public abstract class PostDisposeAbstractModel
{
  @PostDispose
  abstract void doStuff();
}
