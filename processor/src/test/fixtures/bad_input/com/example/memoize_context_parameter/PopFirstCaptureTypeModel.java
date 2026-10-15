package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class PopFirstCaptureTypeModel
{
  @Memoize
  int value() { return 1; }

  @MemoizeContextParameter
  void popContext(int value) {}
  long captureContext() { return 0; }
  void pushContext(int value) {}
}
