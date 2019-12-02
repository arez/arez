package com.example.post_dispose.other;

import arez.annotations.PostDispose;

public interface MultiModelInterface1
{
  @PostDispose
  default void interface1PostDispose1()
  {
  }

  @PostDispose
  default void interface1PostDispose2()
  {
  }
}
