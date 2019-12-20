package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Repository;

// Example of usage of deprecated type through different
// arez annotated methods should cause problems
@Repository( inject = InjectMode.NONE )
@ArezComponent( allowEmpty = true )
@SuppressWarnings( "deprecation" )
public abstract class DeprecatedTypeParameterModel<T extends MyDeprecatedEntity>
{
}
