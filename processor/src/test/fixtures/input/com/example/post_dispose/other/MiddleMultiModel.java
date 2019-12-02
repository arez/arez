package com.example.post_dispose.other;

import arez.annotations.PostDispose;

public abstract class MiddleMultiModel
  extends AbstractMultiModel
  implements MultiModelInterface1, MultiModelInterface3
{
  @PostDispose
  protected void middlePostDispose1()
  {
  }

  @PostDispose
  protected void middlePostDispose2()
  {
  }
}
