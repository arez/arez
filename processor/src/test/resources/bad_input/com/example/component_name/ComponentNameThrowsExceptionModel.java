package com.example.component_name;

import java.io.IOException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentName;

@ArezComponent
public class ComponentNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentName
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
