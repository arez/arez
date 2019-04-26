package com.example.inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import com.example.inheritance.other.BaseCompleteInterfaceModel;
import com.example.inheritance.other.OtherElement;
import javax.annotation.Nullable;

@ArezComponent
public interface CompleteInterfaceModel
  extends BaseCompleteInterfaceModel
{
  @Inverse( name = "parentGeneralisation", referenceName = "child" )
  @Nullable
  OtherElement getParentGeneralisation();
}
