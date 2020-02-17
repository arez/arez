package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Eager;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface ServiceViaEagerStingModelFragment {
  @Nonnull
  @Eager
  @Typed(ServiceViaEagerStingModel.class)
  default ServiceViaEagerStingModel create() {
    return new Arez_ServiceViaEagerStingModel();
  }
}
