package com.example.mapped_relationship;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent(allowEmpty = true, disposeOnDeactivate = true)
public abstract class Target {
  final @Nullable Object source;
  Target(final @Nullable Object source) { this.source = source; }
  public static Target create(final @Nullable Object source) { return new Arez_Target(source); }
}
