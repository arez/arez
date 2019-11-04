package com.example.package_access.other;

import arez.ArezContext;
import arez.annotations.ContextRef;

public abstract class BaseContextRefModel
{
  @ContextRef
  abstract ArezContext getContext();
}
