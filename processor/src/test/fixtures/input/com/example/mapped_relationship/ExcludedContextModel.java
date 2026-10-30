package com.example.mapped_relationship;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ExcludedContextModel extends ContextBase {
  @Override
  @MemoizeContextParameter(pattern = "summary")
  public @Nullable String captureContext() { return null; }
}
