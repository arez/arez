package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@SuppressWarnings("rawtypes")
@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumer10ModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumer10ModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumer10Model.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumer10Model component);
  }
}
