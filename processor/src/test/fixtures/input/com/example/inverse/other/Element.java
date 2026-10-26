package com.example.inverse.other;

import arez.annotations.ArezComponent;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.inverse.PackageAccessWithDifferentPackageInverseModel;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class Element
{
  Element()
  {
  }

  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract @Nullable PackageAccessWithDifferentPackageInverseModel getPackageAccessWithDifferentPackageInverseModel();

  @ReferenceId
  int getPackageAccessWithDifferentPackageInverseModelId()
  {
    return 0;
  }
}
