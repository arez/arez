package com.example.inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import com.example.inheritance.other.BaseCompleteModel;
import com.example.inheritance.other.Element;
import javax.annotation.Nullable;

@ArezComponent
public abstract class CompleteModel
  extends BaseCompleteModel
{
  @Inverse( name = "parentGeneralisation", referenceName = "child" )
  @Nullable
  abstract Element getParentGeneralisation();
}
