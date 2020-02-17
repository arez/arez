package com.example.sting;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import sting.Fragment;
import sting.Named;
import sting.Typed;

@Generated("arez.processor.ArezProcessor")
@Fragment
public interface ServiceViaNamedStingModelFragment {
  @Nonnull
  @Named("")
  @Typed(ServiceViaNamedStingModel.class)
  default ServiceViaNamedStingModel create() {
    return new Arez_ServiceViaNamedStingModel();
  }
}
