package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( allowEmpty = true )
public abstract class MultiComponentNameRefModel
{
  @ComponentNameRef
  abstract String getTypeName();

  @ComponentNameRef
  abstract String getTypeName2();
}
