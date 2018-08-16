package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.LinkType;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultipleReferenceWithInverseWithSameTarget
{
  @ArezComponent
  static abstract class RoleTypeGeneralisation
  {
    private final int _parentId;
    private final int _childId;

    RoleTypeGeneralisation( final int parentId, final int childId )
    {
      _parentId = parentId;
      _childId = childId ;
    }

    @Reference( load = LinkType.EXPLICIT, inverseName = "childGeneralisations", inverseMultiplicity = Multiplicity.MANY )
    @Nonnull
    abstract RoleType getParent();

    @ReferenceId
    final int getParentId()
    {
      return _parentId;
    }

    @Reference( load = LinkType.EXPLICIT, inverseName = "parentGeneralisation", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    @Nonnull
    abstract RoleType getChild();

    @ReferenceId
    final int getChildId()
    {
      return _childId;
    }
  }

  @ArezComponent
  static abstract class RoleType
  {
    @Inverse( name = "childGeneralisations", referenceName = "parent" )
    @Nonnull
    abstract List<RoleTypeGeneralisation> getChildGeneralisations();

    @Inverse( name = "parentGeneralisation", referenceName = "child" )
    @Nullable
    abstract RoleTypeGeneralisation getParentGeneralisation();
  }
}
