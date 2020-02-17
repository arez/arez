package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithAttachOnlyRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithAttachOnlyRepository.class)
  default RepositoryWithAttachOnlyRepository create() {
    return new Arez_RepositoryWithAttachOnlyRepository();
  }
}
