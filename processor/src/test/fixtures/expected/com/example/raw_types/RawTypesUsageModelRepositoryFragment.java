package com.example.raw_types;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface RawTypesUsageModelRepositoryFragment {
  @Nonnull
  @Typed(RawTypesUsageModelRepository.class)
  default RawTypesUsageModelRepository create() {
    return new Arez_RawTypesUsageModelRepository();
  }
}
