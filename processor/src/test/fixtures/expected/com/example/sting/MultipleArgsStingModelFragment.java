package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface MultipleArgsStingModelFragment {
  @Nonnull
  @Typed(MultipleArgsStingModel.class)
  default MultipleArgsStingModel create(final int i, final String foo) {
    return new Arez_MultipleArgsStingModel(i, foo);
  }
}
