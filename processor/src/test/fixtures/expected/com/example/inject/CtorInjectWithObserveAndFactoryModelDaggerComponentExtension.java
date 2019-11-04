package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithObserveAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithObserveAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithObserveAndFactoryModel.Factory createFactory();

    void inject(@Nonnull Arez_CtorInjectWithObserveAndFactoryModel component);
  }
}
