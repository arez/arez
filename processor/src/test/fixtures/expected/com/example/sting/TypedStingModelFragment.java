package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface TypedStingModelFragment {
  @Nonnull
  @Typed({
      Object.class,
      TypedStingModel.class
  })
  default TypedStingModel create() {
    return new Arez_TypedStingModel();
  }
}
