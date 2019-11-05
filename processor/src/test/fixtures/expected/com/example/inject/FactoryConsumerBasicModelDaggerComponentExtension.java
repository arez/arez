package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerBasicModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerBasicModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerBasicModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerBasicModel component);
  }
}
