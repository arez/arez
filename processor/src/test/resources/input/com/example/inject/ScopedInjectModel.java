package com.example.inject;

import javax.inject.Singleton;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@Singleton
@ArezComponent
public class ScopedInjectModel
{
  public ScopedInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
