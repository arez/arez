package com.example.component_state_ref;

import arez.annotations.ComponentStateRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface ComponentStateRefInterface
{
  @ComponentStateRef
  boolean isReady();
}
