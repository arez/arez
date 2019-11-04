package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class ComponentIdExample
{
  @ComponentId
  final int getId()
  {
    return 0;
  }
}
