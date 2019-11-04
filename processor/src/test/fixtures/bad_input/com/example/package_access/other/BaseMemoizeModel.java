package com.example.package_access.other;

import arez.annotations.Memoize;

public abstract class BaseMemoizeModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }
}
