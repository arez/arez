package com.example.inverse.other;

import arez.annotations.ArezComponent;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inverse.PackageAccessWithDifferentPackageInverseModel;

@ArezComponent
public abstract class Element
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  protected abstract PackageAccessWithDifferentPackageInverseModel getPackageAccessWithDifferentPackageInverseModel();

  @ReferenceId
  protected int getPackageAccessWithDifferentPackageInverseModelId()
  {
    return 0;
  }
}
