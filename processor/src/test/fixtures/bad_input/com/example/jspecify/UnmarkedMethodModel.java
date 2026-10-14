package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnmarkedMethodModel {
  @NullUnmarked @Observable abstract String getValue();
  abstract void setValue(String value);
}
