package com.example.package_access.other;

import arez.Component;
import arez.annotations.ComponentRef;
import javax.annotation.Nonnull;

public abstract class BaseComponentRefModel
{
  @Nonnull
  @ComponentRef
  abstract Component getComponent();
}
