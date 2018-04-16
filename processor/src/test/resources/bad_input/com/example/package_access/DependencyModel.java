package com.example.package_access;

import arez.annotations.ArezComponent;
import com.example.package_access.other.BaseDependencyModel;

@ArezComponent( allowEmpty = true )
abstract class DependencyModel
  extends BaseDependencyModel
{
}
