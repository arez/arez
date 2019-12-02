package com.example.post_construct.other;

import arez.annotations.PostConstruct;

public abstract class MiddleMultiModel
  extends AbstractMultiModel
  implements MultiModelInterface1, MultiModelInterface3
{
  @PostConstruct
  protected void middlePostConstruct1()
  {
  }

  @PostConstruct
  protected void middlePostConstruct2()
  {
  }
}
