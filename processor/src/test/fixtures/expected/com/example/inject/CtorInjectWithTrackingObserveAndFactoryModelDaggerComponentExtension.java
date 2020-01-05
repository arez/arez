package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithTrackingObserveAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithTrackingObserveAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithTrackingObserveAndFactoryModel.Factory createFactory();
  }
}
