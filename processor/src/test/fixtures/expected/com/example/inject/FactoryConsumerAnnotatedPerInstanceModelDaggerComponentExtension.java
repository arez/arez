package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerAnnotatedPerInstanceModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerAnnotatedPerInstanceModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerAnnotatedPerInstanceModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerAnnotatedPerInstanceModel component);
  }
}
