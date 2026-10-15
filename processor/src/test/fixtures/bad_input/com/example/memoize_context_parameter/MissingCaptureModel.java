package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MissingCaptureModel
{
  @Memoize
  int value() { return 1; }

  @MemoizeContextParameter
  void pushContext(int value) {}
  void popContext(int value) {}
}
