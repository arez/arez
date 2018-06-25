package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;
import java.io.IOException;

@ArezComponent( allowEmpty = true )
public abstract class ThrowsDependency
{
  @Dependency
  final DisposeTrackable getTime()
    throws IOException
  {
    return null;
  }
}
