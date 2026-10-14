package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings( "rawtypes" )
@ArezComponent( allowEmpty = true )
abstract class RawTypeComponentIdRefModel
{
  @NullMarked
  interface MyId<A extends @Nullable Object, B extends @Nullable Object>
  {
  }

  @ComponentId
  MyId id()
  {
    throw new UnsupportedOperationException("Fixture declaration");
  }

  @ComponentIdRef
  abstract @Nullable MyId getId();
}
