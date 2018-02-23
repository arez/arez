package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import java.io.IOException;

@ArezComponent( allowEmpty = true )
public abstract class ThrowsDependency
{
  @Dependency
  Object getTime()
    throws IOException
  {
    return null;
  }
}
