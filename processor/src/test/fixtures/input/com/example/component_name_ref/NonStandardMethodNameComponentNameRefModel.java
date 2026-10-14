package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class NonStandardMethodNameComponentNameRefModel
{
  @ComponentNameRef
  abstract @Nullable String $$$getComponentName$$$$();
}
