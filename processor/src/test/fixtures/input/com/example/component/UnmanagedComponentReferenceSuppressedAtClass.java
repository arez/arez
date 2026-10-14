package com.example.component;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "Arez:UnmanagedComponentReference" )
@ArezComponent( allowEmpty = true )
abstract class UnmanagedComponentReferenceSuppressedAtClass
{
  final @Nullable DisposeNotifier time = null;
}
