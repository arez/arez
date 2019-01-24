package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface CtorInjectWithPostConstructAndFactoryModelDaggerComponentExtension {
  DaggerSubcomponent getCtorInjectWithPostConstructAndFactoryModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_CtorInjectWithPostConstructAndFactoryModel.Factory createFactory();

    void inject(@Nonnull Arez_CtorInjectWithPostConstructAndFactoryModel component);
  }
}
