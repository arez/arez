package com.example.package_access.other;

import arez.annotations.Computed;

public abstract class BaseComputedModel
{
  @Computed
  long getTime()
  {
    return 0;
  }
}
