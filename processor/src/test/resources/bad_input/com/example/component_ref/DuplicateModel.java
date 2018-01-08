package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class DuplicateModel
{
  @ComponentRef
  Component getComponent()
  {
    throw new IllegalStateException();
  }

  @ComponentRef
  Component getComponent2()
  {
    throw new IllegalStateException();
  }
}
