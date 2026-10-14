package com.example.component_ref;

import arez.Component;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ComponentRefInterface
{
  @ComponentRef
  @Nullable Component getComponent();
}
