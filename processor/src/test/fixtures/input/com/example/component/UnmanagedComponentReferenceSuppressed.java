package com.example.component;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceSuppressed
{
  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  final @Nullable DisposeNotifier time = null;
}
