package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface CompleteRepositoryExampleRepositoryFragment {
  @Nonnull
  @Typed(CompleteRepositoryExampleRepository.class)
  default CompleteRepositoryExampleRepository create() {
    return new Arez_CompleteRepositoryExampleRepository();
  }
}
