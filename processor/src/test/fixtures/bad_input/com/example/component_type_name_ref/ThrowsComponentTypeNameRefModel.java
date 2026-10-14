package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import java.io.IOException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class ThrowsComponentTypeNameRefModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentTypeNameRef
  abstract @Nullable String getTypeName()
    throws IOException;
}
