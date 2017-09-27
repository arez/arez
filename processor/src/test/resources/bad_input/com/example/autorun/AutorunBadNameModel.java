package com.example.autorun;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class AutorunBadNameModel
{
  @Autorun( name = "-ace" )
  void foo()
  {
  }
}
