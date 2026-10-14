package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessComponentTypeNameRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @ComponentTypeNameRef
  protected abstract @Nullable String getTypeName();
}
