package com.example.post_dispose.other;

import arez.annotations.PostDispose;

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
