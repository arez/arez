package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@SuppressWarnings("rawtypes")
@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithRawTypeInjectedSuppressedAtClassModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithRawTypeInjectedSuppressedAtClassModelDaggerSubcomponent(
      );

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel.Factory createFactory();

    void inject(@Nonnull Arez_FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel component);
  }
}
