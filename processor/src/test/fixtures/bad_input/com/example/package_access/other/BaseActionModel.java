package com.example.package_access.other;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseActionModel
{
  @Action
  void somePackageAccessAction()
  {
  }
}
