package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ParameterizedFieldDependencyInParentModel
  extends BaseParameterizedFieldDependencyInParentModel<DisposeNotifier>
{
}
