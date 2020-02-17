package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface DaggerEnabledRepositoryRepositoryFragment {
  @Nonnull
  @Typed(DaggerEnabledRepositoryRepository.class)
  default DaggerEnabledRepositoryRepository create() {
    return new Arez_DaggerEnabledRepositoryRepository();
  }
}
