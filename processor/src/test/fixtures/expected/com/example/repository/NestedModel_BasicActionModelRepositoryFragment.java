package com.example.repository;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface NestedModel_BasicActionModelRepositoryFragment {
  @Nonnull
  @Typed(NestedModel_BasicActionModelRepository.class)
  default NestedModel_BasicActionModelRepository create() {
    return new Arez_NestedModel_BasicActionModelRepository();
  }
}
