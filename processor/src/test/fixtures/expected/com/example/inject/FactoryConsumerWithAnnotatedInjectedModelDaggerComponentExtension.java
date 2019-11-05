package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithAnnotatedInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithAnnotatedInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithAnnotatedInjectedModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerWithAnnotatedInjectedModel component);
  }
}
