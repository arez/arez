package com.example.id;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryExampleRepositoryFragment {
  @Nonnull
  @Typed(RepositoryExampleRepository.class)
  default RepositoryExampleRepository create() {
    return new Arez_RepositoryExampleRepository();
  }
}
