package com.example.component;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;

@ArezComponent( allowEmpty = true )
abstract class UnmanagedDisposeNotifierReference
{
  final DisposeNotifier time = null;
}
