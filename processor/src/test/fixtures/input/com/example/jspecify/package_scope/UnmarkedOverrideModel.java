package com.example.jspecify.package_scope;
import arez.annotations.*;
import org.jspecify.annotations.*;
@NullUnmarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnmarkedOverrideModel {
  @Observable abstract @Nullable String getValue();
  abstract void setValue(@Nullable String value);
}
