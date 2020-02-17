package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithDetachOnlyRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithDetachOnlyRepository.class)
  default RepositoryWithDetachOnlyRepository create() {
    return new Arez_RepositoryWithDetachOnlyRepository();
  }
}
