package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import com.example.post_dispose.other.MiddleMultiModel;
import com.example.post_dispose.other.MultiModelInterface2;
import com.example.post_dispose.other.MultiModelInterface3;

@ArezComponent( allowEmpty = true )
abstract class MultiViaInheritanceChainPostDisposeModel
  extends MiddleMultiModel
  implements MultiModelInterface2, MultiModelInterface3
{
  @PostDispose
  void postDispose1()
  {
  }

  @PostDispose
  void postDispose2()
  {
  }
}
