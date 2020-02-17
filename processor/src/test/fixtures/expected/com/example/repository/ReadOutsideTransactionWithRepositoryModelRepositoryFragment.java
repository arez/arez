package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface ReadOutsideTransactionWithRepositoryModelRepositoryFragment {
  @Nonnull
  @Typed(ReadOutsideTransactionWithRepositoryModelRepository.class)
  default ReadOutsideTransactionWithRepositoryModelRepository create() {
    return new Arez_ReadOutsideTransactionWithRepositoryModelRepository();
  }
}
