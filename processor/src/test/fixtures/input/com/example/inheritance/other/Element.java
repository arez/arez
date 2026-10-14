package com.example.inheritance.other;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inheritance.CompleteModel;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class Element
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract @Nullable CompleteModel getCompleteModel();

  @ReferenceId
  int getCompleteModelId()
  {
    return 0;
  }

  @Reference( load = LinkType.EXPLICIT, inverseName = "parentGeneralisation", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
  @NonNull
  abstract CompleteModel getChild();

  @ReferenceId
  int getChildId()
  {
    return 0;
  }
}
