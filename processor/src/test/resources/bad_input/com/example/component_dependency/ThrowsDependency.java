package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;
import java.io.IOException;

@ArezComponent( allowEmpty = true )
public abstract class ThrowsDependency
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentDependency
  final DisposeTrackable getTime()
    throws IOException
  {
    return null;
  }
}
