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
  String id()
  {
    throw new UnsupportedOperationException("Fixture declaration");
  }

  @ComponentIdRef
  abstract @Nullable String getId();
}
