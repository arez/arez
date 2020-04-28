package com.example.deprecated;

import arez.annotations.ArezComponent;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@ArezComponent( allowEmpty = true )
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedTypeParameterModel<T extends MyDeprecatedEntity>
{
}
