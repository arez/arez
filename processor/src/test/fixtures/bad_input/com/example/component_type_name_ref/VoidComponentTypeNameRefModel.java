package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class VoidComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  abstract void getTypeName();
}
