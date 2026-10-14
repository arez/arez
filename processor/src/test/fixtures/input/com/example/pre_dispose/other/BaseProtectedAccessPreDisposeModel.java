package com.example.pre_dispose.other;

import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessPreDisposeModel
{
  @PreDispose
  protected void preDispose()
  {
  }
}
