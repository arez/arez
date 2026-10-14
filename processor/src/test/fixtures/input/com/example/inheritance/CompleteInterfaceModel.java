package com.example.inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import com.example.inheritance.other.BaseCompleteInterfaceModel;
import com.example.inheritance.other.OtherElement;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public interface CompleteInterfaceModel
  extends BaseCompleteInterfaceModel
{
  @Inverse( name = "parentGeneralisation", referenceName = "child" )
  @Nullable
  OtherElement getParentGeneralisation();
}
