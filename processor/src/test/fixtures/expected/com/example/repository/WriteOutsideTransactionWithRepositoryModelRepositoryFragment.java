package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface WriteOutsideTransactionWithRepositoryModelRepositoryFragment {
  @Nonnull
  @Typed(WriteOutsideTransactionWithRepositoryModelRepository.class)
  default WriteOutsideTransactionWithRepositoryModelRepository create() {
    return new Arez_WriteOutsideTransactionWithRepositoryModelRepository();
  }
}
