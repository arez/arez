package com.example.pre_dispose.other;

import arez.annotations.PreDispose;

public abstract class MiddleMultiModel
  extends AbstractMultiModel
  implements MultiModelInterface1, MultiModelInterface3
{
  @PreDispose
  protected void middlePreDispose1()
  {
  }

  @PreDispose
  protected void middlePreDispose2()
  {
  }
}
