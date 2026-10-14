package com.example.reserved_names;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class IsDisposedReservedNameModel
{
  abstract void isDisposed();
}
