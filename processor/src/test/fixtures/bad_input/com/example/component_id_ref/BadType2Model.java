package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class BadType2Model
{
  @ComponentId
  final short getComponentId()
  {
    return 42;
  }

  @ComponentIdRef
  abstract @Nullable String getId();
}
