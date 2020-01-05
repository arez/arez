package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithRawTypeInjectedSuppressedAtClassModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithRawTypeInjectedSuppressedAtClassModelDaggerSubcomponent(
      );

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel.Factory createFactory();
  }
}
