package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ComponentIdExample
{
  @ComponentId
  int getId()
  {
    return 0;
  }
}
