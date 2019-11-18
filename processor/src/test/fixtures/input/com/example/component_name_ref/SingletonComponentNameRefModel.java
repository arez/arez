package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( nameIncludesId = false, allowEmpty = true )
abstract class SingletonComponentNameRefModel
{
  @ComponentNameRef
  abstract String getComponentName();
}
