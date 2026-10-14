package com.example.misplaced_annotation;

import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ObservableInitialFieldOutsideArezTypeModel
{
  @ObservableInitial
  final @Nullable String INITIAL_NAME = "";
}
