package com.example.package_access;

import arez.annotations.ArezComponent;
import com.example.package_access.other.BaseComponentDependencyModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ComponentDependencyModel
  extends BaseComponentDependencyModel
{
}
