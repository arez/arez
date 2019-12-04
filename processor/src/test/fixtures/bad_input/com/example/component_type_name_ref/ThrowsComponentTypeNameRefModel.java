package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import java.io.IOException;

@ArezComponent( allowEmpty = true )
public abstract class ThrowsComponentTypeNameRefModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentTypeNameRef
  abstract String getTypeName()
    throws IOException;
}
