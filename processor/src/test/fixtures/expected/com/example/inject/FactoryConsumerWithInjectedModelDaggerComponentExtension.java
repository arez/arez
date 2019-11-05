package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithInjectedModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerWithInjectedModel component);
  }
}
