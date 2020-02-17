package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithMultipleCtorsRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithMultipleCtorsRepository.class)
  default RepositoryWithMultipleCtorsRepository create() {
    return new Arez_RepositoryWithMultipleCtorsRepository();
  }
}
