package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Named;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface NamedArgStingModelFragment {
  @Nonnull
  @Typed(NamedArgStingModel.class)
  default NamedArgStingModel create(@Named("port") final int port) {
    return new Arez_NamedArgStingModel(port);
  }
}
