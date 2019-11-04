package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import com.example.inverse.other.Element;
import java.util.List;

@ArezComponent
public abstract class PackageAccessWithDifferentPackageInverseModel
{
  // This must not be public. In a normal scenario this would result in a
  // package access instance of Arez_PackageAccessWithDifferentPackageInverseModel.
  // However as this class references other.Element which in a different package the
  // class Arez_PackageAccessWithDifferentPackageInverseModel must be public
  PackageAccessWithDifferentPackageInverseModel()
  {
  }

  @Observable
  @Inverse
  protected abstract List<Element> getElements();
}
