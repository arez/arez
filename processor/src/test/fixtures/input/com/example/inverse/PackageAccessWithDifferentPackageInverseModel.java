package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import com.example.inverse.other.Element;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class PackageAccessWithDifferentPackageInverseModel
{
  PackageAccessWithDifferentPackageInverseModel()
  {
  }

  @Observable
  @Inverse
  abstract @Nullable List<Element> getElements();
}
