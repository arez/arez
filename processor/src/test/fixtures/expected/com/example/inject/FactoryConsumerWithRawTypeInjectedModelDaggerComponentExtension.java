package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithRawTypeInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithRawTypeInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithRawTypeInjectedModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerWithRawTypeInjectedModel component);
  }
}
