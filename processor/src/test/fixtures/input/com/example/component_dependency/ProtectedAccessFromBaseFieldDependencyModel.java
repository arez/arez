package com.example.component_dependency;

import arez.annotations.ArezComponent;
import com.example.component_dependency.other.BaseProtectedAccessFieldDependencyModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ProtectedAccessFromBaseFieldDependencyModel
  extends BaseProtectedAccessFieldDependencyModel
{
}
