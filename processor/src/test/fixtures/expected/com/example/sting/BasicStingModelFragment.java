package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface BasicStingModelFragment {
  @Nonnull
  @Typed(BasicStingModel.class)
  default BasicStingModel create() {
    return new Arez_BasicStingModel();
  }
}
