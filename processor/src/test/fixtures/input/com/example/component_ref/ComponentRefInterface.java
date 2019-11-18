package com.example.component_ref;

import arez.Component;
import arez.annotations.ComponentRef;

public interface ComponentRefInterface
{
  @ComponentRef
  Component getComponent();
}
