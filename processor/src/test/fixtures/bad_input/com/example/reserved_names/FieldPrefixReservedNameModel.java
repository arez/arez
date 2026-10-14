package com.example.reserved_names;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class FieldPrefixReservedNameModel
{
  abstract void $$arez$$_hello( @Nullable String v );
}
