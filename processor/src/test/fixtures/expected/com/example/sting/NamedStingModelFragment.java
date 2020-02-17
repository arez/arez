package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Named;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface NamedStingModelFragment {
  @Nonnull
  @Named("")
  @Typed(NamedStingModel.class)
  default NamedStingModel create() {
    return new Arez_NamedStingModel();
  }
}
