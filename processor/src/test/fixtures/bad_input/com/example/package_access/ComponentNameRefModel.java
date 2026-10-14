package com.example.package_access;

import arez.annotations.ArezComponent;
import com.example.package_access.other.BaseComponentNameRefModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ComponentNameRefModel
  extends BaseComponentNameRefModel
{
}
