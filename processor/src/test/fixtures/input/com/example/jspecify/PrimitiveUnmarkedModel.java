package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@ArezComponent(sting = Feature.DISABLE)
abstract class PrimitiveUnmarkedModel {
  @Observable abstract int getCount();
  abstract void setCount(int count);
}
