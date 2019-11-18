package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessComponentRefModel
{
  @ComponentRef
  public abstract Component getComponent();
}
