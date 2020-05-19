package com.example.inheritance.other;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inheritance.CompleteInterfaceModel;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class OtherElement
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract CompleteInterfaceModel getCompleteInterfaceModel();

  @ReferenceId
  int getCompleteInterfaceModelId()
  {
    return 0;
  }

  @Reference( load = LinkType.EXPLICIT, inverseName = "parentGeneralisation", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
  @Nonnull
  abstract CompleteInterfaceModel getChild();

  @ReferenceId
  final int getChildId()
  {
    return 0;
  }
}
