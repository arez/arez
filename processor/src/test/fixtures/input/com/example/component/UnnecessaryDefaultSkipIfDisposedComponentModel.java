package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultSkipIfDisposed = Feature.ENABLE, allowEmpty = true )
abstract class UnnecessaryDefaultSkipIfDisposedComponentModel
{
}
