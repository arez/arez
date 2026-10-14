package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class LegacyDecoratedModel {
  @Observable
  @javax.annotation.Nullable
  @org.jetbrains.annotations.Nullable
  abstract String getValue();
  @javax.annotation.Nonnull
  abstract void setValue(@javax.annotation.Nullable String value);
}
