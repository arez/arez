package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public class AutorunBadNameModel
{
  @Autorun( name = "-ace" )
  void foo()
  {
  }
}
