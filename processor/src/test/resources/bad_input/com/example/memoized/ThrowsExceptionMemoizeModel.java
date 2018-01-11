package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.io.IOException;

@ArezComponent
public class ThrowsExceptionMemoizeModel
{
  @Memoize
  int getField( int key )
    throws IOException
  {
    return 0;
  }
}
