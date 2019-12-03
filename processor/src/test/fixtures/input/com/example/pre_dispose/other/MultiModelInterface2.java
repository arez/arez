package com.example.pre_dispose.other;

import arez.annotations.PreDispose;

public interface MultiModelInterface2
{
  @PreDispose
  default void interface2PreDispose1()
  {
  }

  @PreDispose
  default void interface2PreDispose2()
  {
  }
}
