package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithRawTypeInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithRawTypeInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithRawTypeInjectedModel.Factory createFactory();
  }
}
