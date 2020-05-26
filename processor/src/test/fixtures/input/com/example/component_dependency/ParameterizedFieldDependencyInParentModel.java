package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class ParameterizedFieldDependencyInParentModel
  extends BaseParameterizedFieldDependencyInParentModel<DisposeNotifier>
{
}

