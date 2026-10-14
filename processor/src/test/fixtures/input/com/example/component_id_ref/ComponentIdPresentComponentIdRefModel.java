package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ComponentIdPresentComponentIdRefModel
{
  @ComponentId
  int id()
  {
    return 0;
  }

  @ComponentIdRef
  abstract int getId();
}
