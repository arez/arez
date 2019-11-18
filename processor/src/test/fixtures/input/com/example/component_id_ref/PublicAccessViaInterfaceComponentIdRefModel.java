package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;

@ArezComponent( allowEmpty = true )
public abstract class PublicAccessViaInterfaceComponentIdRefModel
  implements ComponentIdRefInterface
{
  @Override
  @ComponentIdRef
  public abstract int getId();
}
