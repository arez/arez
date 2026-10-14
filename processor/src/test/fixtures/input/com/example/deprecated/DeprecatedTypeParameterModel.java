package com.example.deprecated;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@NullMarked
@ArezComponent( allowEmpty = true )
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedTypeParameterModel<T extends MyDeprecatedEntity>
{
}
