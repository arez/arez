package com.example.package_access.other;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BasePostConstructModel
{
  @PostConstruct
  void postConstruct()
  {
  }
}
