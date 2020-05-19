package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;

@ArezComponent( allowEmpty = true )
abstract class NonIntTypeComponentIdRefModel
{
  @ComponentId
  String id()
  {
    return null;
  }

  @ComponentIdRef
  abstract String getId();
}
