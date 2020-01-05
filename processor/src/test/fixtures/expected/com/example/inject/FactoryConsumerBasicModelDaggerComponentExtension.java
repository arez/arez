package com.example.inject;

import dagger.Subcomponent;
import javax.annotation.Generated;

@Generated("arez.processor.ArezProcessor")
public interface FactoryConsumerBasicModelDaggerComponentExtension {
  DaggerSubcomponent getFactoryConsumerBasicModelDaggerSubcomponent();

  @Subcomponent
  interface DaggerSubcomponent {
    Arez_FactoryConsumerBasicModel.Factory createFactory();
  }
}
