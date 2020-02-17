package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithCreateOrAttachRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithCreateOrAttachRepository.class)
  default RepositoryWithCreateOrAttachRepository create() {
    return new Arez_RepositoryWithCreateOrAttachRepository();
  }
}
