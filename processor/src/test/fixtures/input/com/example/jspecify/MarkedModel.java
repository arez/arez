package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class MarkedModel {
  @Observable abstract String getValue();
  abstract void setValue(String value);
  @Observable abstract int getCount();
  abstract void setCount(int count);
}
