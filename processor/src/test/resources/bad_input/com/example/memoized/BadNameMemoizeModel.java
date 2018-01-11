package com.example.memoized;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.io.IOException;

@ArezComponent
public class BadNameMemoizeModel
{
  @Memoize(name = "-ace")
  int getField( int key )
  {
    return 0;
  }
}
