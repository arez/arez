package com.example.component;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;

@SuppressWarnings( "Arez:UnmanagedComponentReference" )
@ArezComponent( allowEmpty = true )
public abstract class UnmanagedComponentReferenceSuppressedAtClass
{
  final DisposeNotifier time = null;
}
