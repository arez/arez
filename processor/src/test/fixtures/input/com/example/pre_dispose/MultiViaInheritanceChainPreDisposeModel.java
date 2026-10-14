package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import com.example.pre_dispose.other.MiddleMultiModel;
import com.example.pre_dispose.other.MultiModelInterface2;
import com.example.pre_dispose.other.MultiModelInterface3;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class MultiViaInheritanceChainPreDisposeModel
  extends MiddleMultiModel
  implements MultiModelInterface2, MultiModelInterface3
{
  @PreDispose
  void preDispose1()
  {
  }

  @PreDispose
  void preDispose2()
  {
  }
}
