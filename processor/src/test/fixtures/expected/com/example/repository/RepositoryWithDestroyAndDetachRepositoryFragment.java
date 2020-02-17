package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithDestroyAndDetachRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithDestroyAndDetachRepository.class)
  default RepositoryWithDestroyAndDetachRepository create() {
    return new Arez_RepositoryWithDestroyAndDetachRepository();
  }
}
