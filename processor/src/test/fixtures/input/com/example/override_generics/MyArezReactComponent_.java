package com.example.override_generics;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class MyArezReactComponent_
  extends MyArezReactComponent
{
}
