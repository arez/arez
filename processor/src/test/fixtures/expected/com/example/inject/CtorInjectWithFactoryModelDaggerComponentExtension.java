package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithFactoryModel.Factory createFactory();

    void inject(@Nonnull Arez_CtorInjectWithFactoryModel component);
  }
}
