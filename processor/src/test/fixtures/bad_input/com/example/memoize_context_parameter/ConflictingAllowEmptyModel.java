package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ConflictingAllowEmptyModel
{
  @Memoize
  int value() { return 1; }

  @MemoizeContextParameter
  int captureContext() { return 0; }
  @MemoizeContextParameter(allowEmpty = true)
  void pushContext(int value) {}
  void popContext(int value) {}
}
