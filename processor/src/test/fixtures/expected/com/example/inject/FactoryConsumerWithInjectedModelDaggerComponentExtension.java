package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithInjectedModel.Factory createFactory();
  }
}
