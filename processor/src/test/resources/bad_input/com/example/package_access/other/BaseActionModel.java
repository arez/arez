package com.example.package_access.other;

import arez.annotations.Action;

public abstract class BaseActionModel
{
  @Action
  void somePackageAccessAction()
  {
  }
}
