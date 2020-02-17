package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface DaggerDisabledRepositoryRepositoryFragment {
  @Nonnull
  @Typed(DaggerDisabledRepositoryRepository.class)
  default DaggerDisabledRepositoryRepository create() {
    return new Arez_DaggerDisabledRepositoryRepository();
  }
}
