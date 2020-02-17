package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithExplicitIdRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithExplicitIdRepository.class)
  default RepositoryWithExplicitIdRepository create() {
    return new Arez_RepositoryWithExplicitIdRepository();
  }
}
