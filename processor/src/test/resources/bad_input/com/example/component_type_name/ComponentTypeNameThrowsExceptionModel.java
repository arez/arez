package com.example.component_type_name;

import java.io.IOException;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentTypeName;

@ArezComponent
public class ComponentTypeNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @ComponentTypeName
  String getTypeName()
    throws IOException
  {
    return null;
  }
}
