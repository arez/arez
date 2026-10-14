package com.example.package_access.other;

import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseComponentNameRefModel
{
  @ComponentNameRef
  abstract @Nullable String getComponentName();
}
