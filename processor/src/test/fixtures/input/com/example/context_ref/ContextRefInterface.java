package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ContextRef;

interface ContextRefInterface
{
  @ContextRef
  ArezContext getContext();
}
