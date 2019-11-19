package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;

@SuppressWarnings( "rawtypes" )
@ArezComponent( allowEmpty = true )
abstract class RawTypeComponentIdRefModel
{
  interface MyId<A, B>
  {
  }

  @ComponentId
  final MyId id()
  {
    return null;
  }

  @ComponentIdRef
  abstract MyId getId();
}
