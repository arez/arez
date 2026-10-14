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
  // NullAway recognizes legacy @Nullable; Arez deliberately follows the marked JSpecify contract.
  @SuppressWarnings("NullAway")
  @javax.annotation.Nonnull
  abstract void setValue(@javax.annotation.Nullable String value);
}
