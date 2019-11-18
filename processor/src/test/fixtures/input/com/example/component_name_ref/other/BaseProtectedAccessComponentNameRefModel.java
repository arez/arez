package com.example.component_name_ref.other;

import arez.annotations.ComponentNameRef;

public abstract class BaseProtectedAccessComponentNameRefModel
{
  @ComponentNameRef
  protected abstract String getComponentName();
}
