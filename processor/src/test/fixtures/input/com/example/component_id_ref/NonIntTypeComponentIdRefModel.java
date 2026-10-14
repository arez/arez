package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class NonIntTypeComponentIdRefModel
{
  @ComponentId
  @Nullable String id()
  {
    return null;
  }

  @ComponentIdRef
  abstract @Nullable String getId();
}
