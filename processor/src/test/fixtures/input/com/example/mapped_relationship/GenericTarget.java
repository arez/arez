package com.example.mapped_relationship;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(allowEmpty = true, disposeOnDeactivate = true)
abstract class GenericTarget<T extends @Nullable Object> {
  final T value;
  GenericTarget(T value) { this.value = value; }
  static <T extends @Nullable Object> GenericTarget<T> create(T value) { return new Arez_GenericTarget<T>(value); }
}
