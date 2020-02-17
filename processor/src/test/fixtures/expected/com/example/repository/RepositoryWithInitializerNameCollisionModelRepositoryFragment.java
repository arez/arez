package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithInitializerNameCollisionModelRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithInitializerNameCollisionModelRepository.class)
  default RepositoryWithInitializerNameCollisionModelRepository create() {
    return new Arez_RepositoryWithInitializerNameCollisionModelRepository();
  }
}
