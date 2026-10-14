package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@ArezComponent(sting = Feature.DISABLE)
abstract class UnmarkedModel {
  @Observable @javax.annotation.Nullable abstract String getValue();
  abstract void setValue(String value);
}
