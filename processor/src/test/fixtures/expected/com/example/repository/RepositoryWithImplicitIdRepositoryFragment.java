package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithImplicitIdRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithImplicitIdRepository.class)
  default RepositoryWithImplicitIdRepository create() {
    return new Arez_RepositoryWithImplicitIdRepository();
  }
}
