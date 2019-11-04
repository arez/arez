package com.example.package_access.other;

import arez.annotations.ComponentNameRef;

public abstract class BaseComponentNameRefModel
{
  @ComponentNameRef
  abstract String getComponentName();
}
