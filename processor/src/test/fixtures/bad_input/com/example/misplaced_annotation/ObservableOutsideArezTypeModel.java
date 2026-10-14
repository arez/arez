package com.example.misplaced_annotation;

import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ObservableOutsideArezTypeModel
{
  @Observable
  @Nullable String getName()
  {
    return "";
  }
}
