package com.example.component_type_name_ref.other;

import arez.annotations.ComponentTypeNameRef;

public abstract class BaseProtectedAccessComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  protected abstract String getTypeName();
}
