package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumer8ModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumer8ModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumer8Model.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumer8Model component);
  }
}
