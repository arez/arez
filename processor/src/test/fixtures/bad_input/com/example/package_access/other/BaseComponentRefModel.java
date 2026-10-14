package com.example.package_access.other;

import arez.Component;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseComponentRefModel
{
  @NonNull
  @ComponentRef
  abstract Component getComponent();
}
