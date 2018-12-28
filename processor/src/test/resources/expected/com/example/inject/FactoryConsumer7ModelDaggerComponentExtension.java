package com.example.inject;

import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumer7ModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumer7ModelDaggerSubcomponent();

  interface DaggerSubcomponent {
    Arez_FactoryConsumer7Model.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumer7Model component);
  }
}
