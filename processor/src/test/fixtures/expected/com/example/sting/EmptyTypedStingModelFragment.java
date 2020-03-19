package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Eager;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface EmptyTypedStingModelFragment {
  @Nonnull
  @Eager
  @Typed({})
  default EmptyTypedStingModel create() {
    return new Arez_EmptyTypedStingModel();
  }
}
