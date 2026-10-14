package com.example.package_access.other;

import arez.annotations.ComponentId;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseComponentIdModel
{
  @ComponentId
  final byte getId()
  {
    return 0;
  }
}
