package com.example.pre_dispose.other;

import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MultiModelInterface1
{
  @PreDispose
  default void interface1PreDispose1()
  {
  }

  @PreDispose
  default void interface1PreDispose2()
  {
  }
}
