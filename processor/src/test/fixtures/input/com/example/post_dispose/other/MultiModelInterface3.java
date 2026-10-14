package com.example.post_dispose.other;

import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MultiModelInterface3
{
  @PostDispose
  default void interface3PostDispose1()
  {
  }

  @PostDispose
  default void interface3PostDispose2()
  {
  }
}
