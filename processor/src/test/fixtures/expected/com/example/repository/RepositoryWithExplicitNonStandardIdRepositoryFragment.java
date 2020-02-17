package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithExplicitNonStandardIdRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithExplicitNonStandardIdRepository.class)
  default RepositoryWithExplicitNonStandardIdRepository create() {
    return new Arez_RepositoryWithExplicitNonStandardIdRepository();
  }
}
