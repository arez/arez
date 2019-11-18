package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessComponentNameRefModel
{
  @ComponentNameRef
  public abstract String getComponentName();
}
