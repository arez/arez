package com.example.package_access.other;

import arez.annotations.ComponentId;

public abstract class BaseComponentIdModel
{
  @ComponentId
  final byte getId()
  {
    return 0;
  }
}
