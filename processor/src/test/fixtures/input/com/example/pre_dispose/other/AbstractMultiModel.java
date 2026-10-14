package com.example.pre_dispose.other;

import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractMultiModel
{
  @PreDispose
  protected void abstractPreDispose1()
  {
  }

  @PreDispose
  protected void abstractPreDispose2()
  {
  }
}
