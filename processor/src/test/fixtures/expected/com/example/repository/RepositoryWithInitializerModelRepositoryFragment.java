package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithInitializerModelRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithInitializerModelRepository.class)
  default RepositoryWithInitializerModelRepository create() {
    return new Arez_RepositoryWithInitializerModelRepository();
  }
}
