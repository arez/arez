package com.example.pre_dispose.other;

import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MultiModelInterface3
{
  @PreDispose
  default void interface3PreDispose1()
  {
  }

  @PreDispose
  default void interface3PreDispose2()
  {
  }
}
