package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithRawTypeRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithRawTypeRepository.class)
  default RepositoryWithRawTypeRepository create() {
    return new Arez_RepositoryWithRawTypeRepository();
  }
}
