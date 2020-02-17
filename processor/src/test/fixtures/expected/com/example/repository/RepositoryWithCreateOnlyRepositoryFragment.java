package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithCreateOnlyRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithCreateOnlyRepository.class)
  default RepositoryWithCreateOnlyRepository create() {
    return new Arez_RepositoryWithCreateOnlyRepository();
  }
}
