package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@ArezComponentLike
abstract class UnmarkedBase {
  @Observable public abstract String getValue();
  public abstract void setValue(String value);
}
