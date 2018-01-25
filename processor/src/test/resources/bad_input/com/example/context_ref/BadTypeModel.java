package com.example.context_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
abstract class BadTypeModel
{
  @ContextRef
  abstract String getContext();
}
