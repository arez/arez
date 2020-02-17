package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RepositoryWithDetachNoneRepositoryFragment {
  @Nonnull
  @Typed(RepositoryWithDetachNoneRepository.class)
  default RepositoryWithDetachNoneRepository create() {
    return new Arez_RepositoryWithDetachNoneRepository();
  }
}
