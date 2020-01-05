package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerWithAnnotatedInjectedModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerWithAnnotatedInjectedModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerWithAnnotatedInjectedModel.Factory createFactory();
  }
}
