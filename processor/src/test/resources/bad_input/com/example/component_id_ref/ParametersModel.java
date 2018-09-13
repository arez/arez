package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;

@ArezComponent( allowEmpty = true )
public abstract class ParametersModel
{
  @ComponentIdRef
  abstract int getId( int i );
}
