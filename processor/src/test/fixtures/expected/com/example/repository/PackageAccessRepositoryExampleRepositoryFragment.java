package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface PackageAccessRepositoryExampleRepositoryFragment {
  @Nonnull
  @Typed(PackageAccessRepositoryExampleRepository.class)
  default PackageAccessRepositoryExampleRepository create() {
    return new Arez_PackageAccessRepositoryExampleRepository();
  }
}
