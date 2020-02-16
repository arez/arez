package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@Repository( dagger = Feature.DISABLE )
@ArezComponent( allowEmpty = true )
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedTypeParameterModel<T extends MyDeprecatedEntity>
{
}
