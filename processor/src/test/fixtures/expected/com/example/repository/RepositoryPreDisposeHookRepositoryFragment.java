package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryPreDisposeHookRepositoryFragment {
  @Nonnull
  @Typed(RepositoryPreDisposeHookRepository.class)
  default RepositoryPreDisposeHookRepository create() {
    return new Arez_RepositoryPreDisposeHookRepository();
  }
}
