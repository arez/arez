package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import com.example.post_construct.other.MiddleMultiModel;
import com.example.post_construct.other.MultiModelInterface2;
import com.example.post_construct.other.MultiModelInterface3;

@ArezComponent( allowEmpty = true )
public abstract class MultiViaInheritanceChainPostConstructModel
  extends MiddleMultiModel
  implements MultiModelInterface2, MultiModelInterface3
{
  @PostConstruct
  void postConstruct1()
  {
  }

  @PostConstruct
  void postConstruct2()
  {
  }
}
