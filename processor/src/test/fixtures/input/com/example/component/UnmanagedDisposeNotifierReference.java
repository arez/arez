package com.example.component;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class UnmanagedDisposeNotifierReference
{
  final @Nullable DisposeNotifier time = null;
}
