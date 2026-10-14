package com.example.inheritance;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import com.example.inheritance.other.BaseCompleteModel;
import com.example.inheritance.other.Element;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class CompleteModel
  extends BaseCompleteModel
{
  @Inverse( name = "parentGeneralisation", referenceName = "child" )
  @Nullable
  abstract Element getParentGeneralisation();
}
