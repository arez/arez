package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface ServiceViaTypedStingModelFragment {
  @Nonnull
  @Typed(ServiceViaTypedStingModel.class)
  default ServiceViaTypedStingModel create() {
    return new Arez_ServiceViaTypedStingModel();
  }
}
