package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithTrackingObserveAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithTrackingObserveAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithTrackingObserveAndFactoryModel.Factory createFactory();

    void inject(@Nonnull Arez_CtorInjectWithTrackingObserveAndFactoryModel component);
  }
}
