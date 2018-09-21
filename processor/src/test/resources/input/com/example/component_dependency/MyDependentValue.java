package com.example.component_dependency;

import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

public class MyDependentValue
  implements DisposeTrackable
{
  @Nonnull
  @Override
  public DisposeNotifier getNotifier()
  {
    return new DisposeNotifier();
  }
}
