package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import java.io.IOException;

@ArezComponent( allowEmpty = true )
public abstract class ThrowsDependency
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentDependency
  final DisposeNotifier getTime()
    throws IOException
  {
    return null;
  }
}
