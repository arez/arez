package com.example.inject;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Named;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface DualNamedArgInjectModelFragment {
  @Nonnull
  @Typed(DualNamedArgInjectModel.class)
  default DualNamedArgInjectModel create(@Named("Port") final int port) {
    return new Arez_DualNamedArgInjectModel(port);
  }
}
