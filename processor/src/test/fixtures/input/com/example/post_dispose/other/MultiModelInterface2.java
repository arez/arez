package com.example.post_dispose.other;

import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MultiModelInterface2
{
  @PostDispose
  default void interface2PostDispose1()
  {
  }

  @PostDispose
  default void interface2PostDispose2()
  {
  }
}
