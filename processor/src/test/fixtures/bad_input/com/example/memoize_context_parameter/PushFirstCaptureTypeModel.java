package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class PushFirstCaptureTypeModel
{
  @Memoize
  int value() { return 1; }

  @MemoizeContextParameter
  void pushContext(int value) {}
  long captureContext() { return 0; }
  void popContext(int value) {}
}
