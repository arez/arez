package com.example.deprecated;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface DeprecatedUsageModelRepositoryFragment {
  @Nonnull
  @Typed(DeprecatedUsageModelRepository.class)
  default DeprecatedUsageModelRepository create() {
    return new Arez_DeprecatedUsageModelRepository();
  }
}
