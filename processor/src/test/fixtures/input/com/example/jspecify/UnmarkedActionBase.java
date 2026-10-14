package com.example.jspecify;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

@NullMarked
class UnmarkedActionBase {
  @NullUnmarked
  @Action
  String inherited(String value) { return value; }
}
