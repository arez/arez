package com.example.package_access.other;

import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseMemoizeModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }
}
