package com.example.inheritance.other;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inheritance.CompleteModel;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class Element
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract CompleteModel getCompleteModel();

  @ReferenceId
  int getCompleteModelId()
  {
    return 0;
  }

  @Reference( load = LinkType.EXPLICIT, inverseName = "parentGeneralisation", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
  @Nonnull
  abstract CompleteModel getChild();

  @ReferenceId
  final int getChildId()
  {
    return 0;
  }
}
