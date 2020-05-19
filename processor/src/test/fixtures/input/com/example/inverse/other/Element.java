package com.example.inverse.other;

import arez.annotations.ArezComponent;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inverse.PackageAccessWithDifferentPackageInverseModel;

@ArezComponent
public abstract class Element
{
  // This is package access. Normally this would result in Arez_Element being
  // package access but as it is accessed as an inverse Arez_Element is made public
  Element()
  {
  }

  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract PackageAccessWithDifferentPackageInverseModel getPackageAccessWithDifferentPackageInverseModel();

  @ReferenceId
  int getPackageAccessWithDifferentPackageInverseModelId()
  {
    return 0;
  }
}
