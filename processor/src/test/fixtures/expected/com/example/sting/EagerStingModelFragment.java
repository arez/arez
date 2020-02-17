package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Eager;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface EagerStingModelFragment {
  @Nonnull
  @Eager
  @Typed(EagerStingModel.class)
  default EagerStingModel create() {
    return new Arez_EagerStingModel();
  }
}
