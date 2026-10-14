package com.example.inheritance.other;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inheritance.CompleteInterfaceModel;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class OtherElement
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract @Nullable CompleteInterfaceModel getCompleteInterfaceModel();

  @ReferenceId
  int getCompleteInterfaceModelId()
  {
    return 0;
  }

  @Reference( load = LinkType.EXPLICIT, inverseName = "parentGeneralisation", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
  @NonNull
  abstract CompleteInterfaceModel getChild();

  @ReferenceId
  int getChildId()
  {
    return 0;
  }
}
