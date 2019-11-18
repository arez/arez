package com.example.component_state_ref;

import arez.annotations.ComponentStateRef;

interface ComponentStateRefInterface
{
  @ComponentStateRef
  boolean isReady();
}
