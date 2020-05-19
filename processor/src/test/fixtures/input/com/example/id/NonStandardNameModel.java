package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class NonStandardNameModel
{
  @ComponentId
  int $Id$$()
  {
    return 0;
  }
}
