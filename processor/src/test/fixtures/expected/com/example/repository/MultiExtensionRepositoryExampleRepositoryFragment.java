package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface MultiExtensionRepositoryExampleRepositoryFragment {
  @Nonnull
  @Typed(MultiExtensionRepositoryExampleRepository.class)
  default MultiExtensionRepositoryExampleRepository create() {
    return new Arez_MultiExtensionRepositoryExampleRepository();
  }
}
